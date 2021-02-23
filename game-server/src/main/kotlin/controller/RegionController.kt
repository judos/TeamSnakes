package controller

import ch.judos.snakes.common.dto.AuthSuccessDto
import model.configuration.AppConfig
import model.configuration.Connection
import org.apache.logging.log4j.LogManager
import service.RandomService


class RegionController(
	private val http: HttpController,
	private val config: AppConfig,
	private val random: RandomService
) {
	private val logger = LogManager.getLogger(javaClass)!!

	private var jwt: String? = null
	private var regionToken: String = ""
	private var connection: Connection? = null
	private val connectionAccess = Object()

	fun register() {
		regionToken = random.generateToken(64)
		val data = config.region.getLoginRequest()
		try {
			logger.info("Connecting to region...")
			val response = this.http.post("authenticate", data, AuthSuccessDto::class.java)
			this.jwt = response.jwt
			logger.info("Login to region successful, waiting for connection...")
		} catch (exception: Exception) {
			throw RuntimeException("Couldn't authenticate to region server", exception)
		}
	}

	fun isConnected(): Boolean {
		return this.connection != null
	}

	fun acceptConnection(connection: Connection, hello: String) {
		synchronized(connectionAccess) {
			if (this.connection != null) {
				logger.warn("Already got region connection, declining incoming region connection")
				connection.close()
				return
			}
			if (hello != "region:$regionToken") {
				logger.error("Invalid region token, dropping connection")
				connection.close()
				return
			}
			this.connection = connection
			listenToRegionConnection()
		}
	}

	private fun listenToRegionConnection() {
		val connection = this.connection!!
		val regionListener = Thread({
			var input: String
			try {
				do {
					input = connection.inp.readLine()

				} while (true)
			} finally {
				synchronized(connectionAccess) {
					this.connection = null
				}
			}
		}, "Region Connection")
		regionListener.isDaemon = true
		regionListener.start()
	}

	fun reportServerStats(lobby: LobbyController) {
		synchronized(connectionAccess) {
			if (this.connection == null) {
				return
			}
			val connection = this.connection!!
			connection.out.println("stats")
			connection.out.println(lobby.getStats())
		}
	}

}
