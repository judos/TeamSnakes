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
import java.net.ServerSocket
import java.net.SocketException

@Service
class ClientService(
		private val config: RegionConfig,
		private val userTokenService: UserTokenService
) {
	private val logger = LoggerFactory.getLogger(javaClass)

	private val clients = mutableMapOf<String, Connection>()

	init {
		this.listenToNewConnections()
	}

	private fun listenToNewConnections() {
		logger.info("Listening for connection on tcp-port: ${this.config.port}")
		val socket = ServerSocket(this.config.port)
		val listenThread = Thread({
			while (true) {
				val connectionSocket = socket.accept()!!
				logger.info("new connection with timeout ${connectionSocket.soTimeout}")
				connectionSocket.keepAlive = true
				connectionSocket.soTimeout = 0
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
		}, "Connection Listener")
		listenThread.isDaemon = true
		listenThread.start()
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
				do {
					data = connection.inp.readUnshared()
					logger.info("unknown msg from client: $data")
				} while (true)
			} catch (e: SocketException) {
				logger.info("Client $username connection lost: $e")
			} catch (e: EOFException) {
				logger.info("Client $username connection ended")
			}
		}, "Client Connection $username")
		clientListener.isDaemon = true
		clientListener.start()
	}

}