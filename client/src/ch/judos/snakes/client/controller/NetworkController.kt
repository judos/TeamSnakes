package ch.judos.snakes.client.controller

import ch.judos.snakes.client.model.ClientConfig
import ch.judos.snakes.client.model.GameData
import ch.judos.snakes.common.controller.HttpController
import ch.judos.snakes.common.dto.GuestLoginRequestDto
import ch.judos.snakes.common.dto.UserAuthSuccessDto
import ch.judos.snakes.common.messages.client.ClientListMsg
import ch.judos.snakes.common.messages.client.LobbyListMsg
import ch.judos.snakes.common.messages.region.ClientLogin
import ch.judos.snakes.common.messages.region.LobbyCreateMsg
import ch.judos.snakes.common.model.Connection
import ch.judos.snakes.common.model.Lobby
import org.apache.logging.log4j.LogManager
import java.io.EOFException
import java.net.ConnectException
import java.net.Socket
import java.net.SocketException
import java.util.function.Consumer

class NetworkController(
		private val config: ClientConfig,
		private val gameData: GameData
) {

	private val logger = LogManager.getLogger(javaClass)!!

	var regionConnectionLost: (() -> Unit)? = null
	private var lobbyCreateListener: Consumer<Lobby>? = null
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
					} else if (data is Lobby) {
						this.lobbyCreateListener?.accept(data)
						this.lobbyCreateListener = null
					} else if (data is LobbyListMsg) {
						this.gameData.lobbyData.lobbyList = data.lobbies
					} else {
						logger.info("unknown msg from region: ${data.javaClass} $data")
					}
				} while (true)
			} catch (e: SocketException) {
				logger.info("Region connection lost: $e")
				regionConnectionLost?.invoke()
			} catch (e: EOFException) {
				logger.info("Region connection ended")
				regionConnectionLost?.invoke()
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

	fun createLobby(lobbyName: String, mode: String, consumer: Consumer<Lobby>) {
		val msg = LobbyCreateMsg(lobbyName, mode)
		if (this.regionConnection == null)
			throw RuntimeException("No connection to region server")
		if (this.lobbyCreateListener != null)
			throw RuntimeException("Lobby creation already in progress")
		this.lobbyCreateListener = consumer
		this.regionConnection!!.out.writeUnshared(msg)
	}

}