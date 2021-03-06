package ch.judos.snakes.client.controller

import ch.judos.snakes.client.model.ClientConfig
import ch.judos.snakes.client.model.ClientSettings
import ch.judos.snakes.common.controller.HttpController
import ch.judos.snakes.common.dto.GuestLoginRequestDto
import ch.judos.snakes.common.dto.UserAuthSuccessDto
import ch.judos.snakes.common.messages.region.ClientLogin
import ch.judos.snakes.common.model.Connection
import org.apache.logging.log4j.LogManager
import java.io.EOFException
import java.net.Socket
import java.net.SocketException

class NetworkController(
		private val settings: ClientSettings,
		private val config: ClientConfig,
) {

	private val logger = LogManager.getLogger(javaClass)!!
	private var regionConnection: Connection? = null


	private var tokens = mutableListOf<String>()
	private val http = HttpController(config.region.run {
		"$httpProtocol$url:$httpPort"
	})

	fun login() {
		val data = GuestLoginRequestDto(settings.name!!)
		val response = this.http.post("authenticate/guest", data, UserAuthSuccessDto::class.java)
		this.http.jwt = response.jwt
		this.tokens = response.tokens
		logger.info("Login successful, establishing tcp connection")

		val socket = Socket(config.region.url, config.region.tcpPort)
		val regionConnection = Connection(socket) {}
		regionConnection.out.writeUnshared(ClientLogin(settings.name!!, this.tokens.removeAt(0)))
		this.listenToRegionConnection(regionConnection)
	}

	private fun listenToRegionConnection(regionConnection: Connection) {
		this.regionConnection = regionConnection
		val clientListener = Thread({
			var data: Any
			try {
				do {
					data = regionConnection.inp.readUnshared()
					logger.info("unknown msg from region: $data")
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


}