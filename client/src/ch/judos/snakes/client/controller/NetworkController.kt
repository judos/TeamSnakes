package ch.judos.snakes.client.controller

import ch.judos.snakes.client.model.ClientConfig
import ch.judos.snakes.client.model.GameData
import ch.judos.snakes.common.controller.HttpController
import ch.judos.snakes.common.dto.GuestLoginRequestDto
import ch.judos.snakes.common.dto.UserAuthSuccessDto
import ch.judos.snakes.common.messages.client.ClientListMsg
import ch.judos.snakes.common.messages.region.ClientLogin
import ch.judos.snakes.common.model.Connection
import org.apache.logging.log4j.LogManager
import java.io.EOFException
import java.net.ConnectException
import java.net.Socket
import java.net.SocketException

class NetworkController(
		private val config: ClientConfig,
		private val gameData: GameData
) {

	private val logger = LogManager.getLogger(javaClass)!!
	private var regionConnection: Connection? = null


	private var tokens = mutableListOf<String>()
	private val http = HttpController(config.region.run {
		"$httpProtocol$url:$httpPort"
	})

	private fun listenToRegionConnection(regionConnection: Connection) {
		this.regionConnection = regionConnection
		val clientListener = Thread({
			var data: Any
			try {
				do {
					data = regionConnection.inp.readUnshared()
					if (data is ClientListMsg) {
						this.gameData.playerData.playerList = data.players
					} else {
						logger.info("unknown msg from region: ${data.javaClass} $data")
					}
				} while (true)
			} catch (e: SocketException) {
				logger.info("Region connection lost: $e")
			} catch (e: EOFException) {
				logger.info("Region connection ended")
			}
		}, "Region Connection")
		clientListener.isDaemon = true
		clientListener.start()
	}


	fun login() {
		var connected = false
		do {
			val data = GuestLoginRequestDto(gameData.settings.name!!)
			try {
				gameData.loadingData.set("Connecting to region server...")
				val response = this.http.post("authenticate/guest", data, UserAuthSuccessDto::class.java)
				this.http.jwt = response.jwt
				this.tokens = response.tokens
				logger.info("Login successful, establishing tcp connection")

				val socket = Socket(config.region.url, config.region.tcpPort)
				val regionConnection = Connection(socket) {}
				regionConnection.out.writeUnshared(ClientLogin(gameData.settings.name!!, this.tokens.removeAt(0)))
				this.listenToRegionConnection(regionConnection)
				connected = true
			} catch (e: ConnectException) {
				for (i in 3 downTo 1) {
					gameData.loadingData.current = listOf("Connection to region server failed", "Trying again in ${i}s...")
					Thread.sleep(1000)
				}
			}
		} while (!connected)
	}

}