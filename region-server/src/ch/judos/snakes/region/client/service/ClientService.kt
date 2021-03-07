package ch.judos.snakes.region.client.service

import ch.judos.snakes.common.messages.ErrorMsg
import ch.judos.snakes.common.messages.client.ClientListMsg
import ch.judos.snakes.common.messages.region.ClientLogin
import ch.judos.snakes.common.model.Connection
import ch.judos.snakes.region.core.config.RegionConfig
import ch.judos.snakes.region.core.service.UserTokenService
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
		private val userTokenService: UserTokenService
) {
	private val logger = LoggerFactory.getLogger(javaClass)


	private var serverSocket: ServerSocket? = null
	private var running = true

	private val clients = mutableMapOf<String, Connection>()


	init {
		this.listenToNewConnections()
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
		logger.info("new connection with timeout ${connectionSocket.soTimeout}")
		val connection = Connection(connectionSocket, {})
		val hello = connection.inp.readUnshared()
		logger.info("New connection: $hello")
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
		this.clients[username] = connection
		// send list to all players
		val clientList = ClientListMsg(this.clients.keys.toList())
		this.clients.values.forEach { it.out.writeUnshared(clientList) }

		this.listenToClientConnection(connection, username)
	}

	private fun listenToClientConnection(connection: Connection, username: String) {
		val clientListener = Thread({
			var data: Any
			try {
				while (true) {
					data = connection.inp.readUnshared()
					logger.info("unknown msg from client: $data")
				}
			} catch (e: SocketException) {
				logger.info("Client $username connection lost: $e")
			} catch (e: EOFException) {
				logger.info("Client $username connection ended")
			}
			this.clients.remove(username)
			val clientList = ClientListMsg(this.clients.keys.toList())
			this.clients.values.forEach { it.out.writeUnshared(clientList) }
		}, "Client Connection $username")
		clientListener.isDaemon = true
		clientListener.start()
	}

}