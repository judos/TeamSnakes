package ch.judos.snakes.region.client.service

import ch.judos.snakes.common.messages.ErrorMsg
import ch.judos.snakes.common.messages.client.ClientListMsg
import ch.judos.snakes.common.messages.client.LobbyListMsg
import ch.judos.snakes.common.messages.game.GameLobbyCreateMsg
import ch.judos.snakes.common.messages.region.ClientLogin
import ch.judos.snakes.common.messages.region.LobbyCreateMsg
import ch.judos.snakes.common.model.Client
import ch.judos.snakes.common.model.Connection
import ch.judos.snakes.common.model.Lobby
import ch.judos.snakes.common.service.RandomService
import ch.judos.snakes.region.core.config.RegionConfig
import ch.judos.snakes.region.core.service.UserTokenService
import ch.judos.snakes.region.gameserver.service.GameServerService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.EOFException
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import javax.annotation.PreDestroy

@Service
class ClientService(
		private val config: RegionConfig,
		private val userTokenService: UserTokenService,
		private val gameServerService: GameServerService,
		private val randomService: RandomService
) {
	private val logger = LoggerFactory.getLogger(javaClass)


	private var serverSocket: ServerSocket? = null
	private var running = true

	private val clients = mutableMapOf<String, Client>()


	init {
		this.listenToNewConnections()
		gameServerService.clientService = this
	}

	@PreDestroy
	fun destroy() {
		running = false
		serverSocket?.close()
		serverSocket = null
	}

	private fun listenToNewConnections() {
		val listenThread = Thread({
			while (running) {
				if (serverSocket == null) {
					try {
						this.serverSocket = ServerSocket(this.config.port)
						logger.info("Listening for connection on tcp-port: ${this.config.port}")
					} catch(e: IOException) {
						this.logger.warn("Could not open Serversocket: "+e.message)
						Thread.sleep(5000)
					}
				}
				else {
					try {
						val connectionSocket = serverSocket!!.accept()!!
						this.acceptConnection(connectionSocket)
					} catch (e: SocketException) {
						if (this.running) {
							e.printStackTrace()
						}
					}
				}
			}
		}, "Connection Listener")
		listenThread.isDaemon = true
		listenThread.start()
	}

	private fun acceptConnection(connectionSocket: Socket) {
		val connection = Connection(connectionSocket, {})
		val hello = connection.inp.readUnshared()
		if (hello is ClientLogin) {
			if (this.userTokenService.isValid(hello.username, hello.token)) {
				this.acceptClientConnection(connection, hello.username)
			} else {
				connection.out.writeUnshared(ErrorMsg("INVALID_TOKEN", "Token or username unknown."))
				connection.close()
			}
		} else {
			logger.error("Invalid hello obj from connection: $hello")
			connection.close()
		}
	}

	private fun acceptClientConnection(connection: Connection, username: String) {
		logger.info("Client logged in: $username")
		val client = Client(connection, username)
		this.clients[username] = client
		// send list to all players
		val clientList = ClientListMsg(this.clients.keys.toList())
		this.clients.values.forEach { it.connection.out.writeUnshared(clientList) }

		this.listenToClientConnection(client)
	}

	private fun listenToClientConnection(client: Client) {
		val clientListener = Thread({
			var data: Any
			try {
				while (true) {
					data = client.connection.inp.readUnshared()
					if (data is LobbyCreateMsg) {
						createLobby(client, data)
					}
					else {
						logger.info("unknown msg from client: ${data::class.simpleName} $data")
					}
				}
			} catch (e: SocketException) {
				logger.info("Client ${client.name} connection lost: $e")
			} catch (e: EOFException) {
				logger.info("Client ${client.name} connection ended")
			}
			this.clients.remove(client.name)
			val clientList = ClientListMsg(this.clients.keys.toList())
			this.clients.values.forEach { it.connection.out.writeUnshared(clientList) }
		}, "Client Connection ${client.name}")
		clientListener.isDaemon = true
		clientListener.start()
	}

	private fun createLobby(client: Client, data: LobbyCreateMsg) {
		val gameServer = this.gameServerService.chooseServerForLobby(data.mode)
		val lobbyId = this.randomService.generateToken(32)
		gameServer.connection!!.out.writeUnshared(GameLobbyCreateMsg(data.name, data.mode, lobbyId))
		val lobbyInfo = Lobby(data.name, lobbyId, gameServer.host, gameServer.port)
		client.connection.out.writeUnshared(lobbyInfo)
	}

	fun sendLobbyUpdates() {
		val lobbyList = LobbyListMsg(this.gameServerService.getLobbies())
		for ((_, client) in this.clients) {
			client.connection.out.writeUnshared(lobbyList)
		}
	}

}