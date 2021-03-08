package ch.judos.snakes.client.controller

import ch.judos.snakes.client.model.ClientConfig
import ch.judos.snakes.client.model.ClientSettings
import ch.judos.snakes.client.model.LoadingData
import ch.judos.snakes.common.controller.HttpController
import ch.judos.snakes.common.dto.GuestLoginRequestDto
import ch.judos.snakes.common.dto.UserAuthSuccessDto
import ch.judos.snakes.common.messages.region.ClientLogin
import ch.judos.snakes.common.model.Connection
import org.apache.logging.log4j.LogManager
import java.io.EOFException
import java.net.ConnectException
import java.net.Socket
import java.net.SocketException

class NetworkController(
		private val settings: ClientSettings,
		private val config: ClientConfig,
		private val loadingData: LoadingData,
) {

	private val logger = LogManager.getLogger(javaClass)!!
	private var regionConnection: Connection? = null


	private var tokens = mutableListOf<String>()
	private val http = HttpController(config.region.run {
		"$httpProtocol$url:$httpPort"
	})

	fun login() {
		var connected = false
		do {
			val data = GuestLoginRequestDto(settings.name!!)
			try {
				loadingData.set("Connecting to region server...")
				val response = this.http.post("authenticate/guest", data, UserAuthSuccessDto::class.java)
				this.http.jwt = response.jwt
				this.tokens = response.tokens
				logger.info("Login successful, establishing tcp connection")

				val socket = Socket(config.region.url, config.region.tcpPort)
				val regionConnection = Connection(socket) {}
				regionConnection.out.writeUnshared(ClientLogin(settings.name!!, this.tokens.removeAt(0)))
				this.listenToRegionConnection(regionConnection)
				connected = true
			} catch (e: ConnectException) {
				for (i in 3 downTo 1) {
					loadingData.current = listOf("Connection to region server failed", "Trying again in ${i}s...")
					Thread.sleep(1000)
				}
			}
		} while(!connected)
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