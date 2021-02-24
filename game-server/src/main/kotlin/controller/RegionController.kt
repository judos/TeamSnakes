package controller

import ch.judos.snakes.common.dto.AuthSuccessDto
import ch.judos.snakes.common.dto.GameserverConnectDto
import ch.judos.snakes.common.messages.game.RegionLogin
import ch.judos.snakes.common.messages.region.GameUpdate
import ch.judos.snakes.common.model.Connection
import model.configuration.AppConfig
import org.apache.logging.log4j.LogManager
import service.RandomService
import java.lang.management.ManagementFactory


class RegionController(
	private val http: HttpController,
	private val config: AppConfig,
	private val random: RandomService,
	private val game: GameController
) {
	private val logger = LogManager.getLogger(javaClass)!!

	var serverNr: Int? = null

	private var regionToken: String = ""
	private var connection: Connection? = null

	fun register() {
		regionToken = random.generateToken(64)
		val data = config.region.getLoginRequest()
		try {
			logger.info("Connecting to region...")
			val response = this.http.post("authenticate", data, AuthSuccessDto::class.java)
			this.http.jwt = response.jwt
			logger.info("Login to region successful, waiting for connection...")

			val data2 = GameserverConnectDto(regionToken, config.server.url, config.server.port,
				game.getSupportedGameModes())
			this.serverNr = this.http.post("gameserver/connect", data2, Int::class.java)
			logger.info("Received server nr: ${this.serverNr}")

		} catch (exception: Exception) {
			logger.warn("Couldn't authenticate to region server: $exception")
		}
	}

	fun isConnected(): Boolean {
		return this.connection != null
	}

	fun acceptConnection(connection: Connection, hello: RegionLogin) {
		if (this.connection != null) {
			logger.warn("Already got region connection, declining incoming region connection")
			connection.close()
			return
		}
		if (hello.regionToken != this.regionToken) {
			logger.error("Invalid region token, dropping connection")
			connection.close()
			return
		}
		logger.info("Accepted region connection")
		this.connection = connection
		listenToRegionConnection()
	}

	private fun listenToRegionConnection() {
		val connection = this.connection!!
		val regionListener = Thread({
			var data: Any
			try {
				do {
					data = connection.inp.readUnshared()
					logger.info("unknown msg from region: $data")
				} while (true)
			} finally {
				this.connection = null
			}
		}, "Region Connection")
		regionListener.isDaemon = true
		regionListener.start()
	}

	fun reportServerStats(lobby: LobbyController) {
		val connection = this.connection ?: return
		val bean = ManagementFactory.getOperatingSystemMXBean()
		val loadAvg = if (bean.systemLoadAverage < 0) 0.8 else bean.systemLoadAverage
		connection.out.writeUnshared(GameUpdate(loadAvg, lobby.getLobbiesInfo()))
	}

}
