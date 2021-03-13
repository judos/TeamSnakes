package ch.judos.snakes.client.controller

import ch.judos.snakes.client.model.ClientConfig
import ch.judos.snakes.client.model.GameData
import ch.judos.snakes.client.model.event.RegionConnectionLost
import ch.judos.snakes.common.controller.HttpController
import ch.judos.snakes.common.dto.GuestLoginRequestDto
import ch.judos.snakes.common.dto.UserAuthSuccessDto
import ch.judos.snakes.common.messages.client.ClientListMsg
import ch.judos.snakes.common.messages.client.LobbyListMsg
import ch.judos.snakes.common.messages.client.LobbyNotCreatedMsg
import ch.judos.snakes.common.messages.region.ClientLogin
import ch.judos.snakes.common.messages.region.LobbyCreateMsg
import ch.judos.snakes.common.model.Connection
import ch.judos.snakes.common.model.Lobby
import ch.judos.snakes.common.service.Observable
import ch.judos.snakes.common.service.ObservableI
import org.apache.logging.log4j.LogManager
import java.io.EOFException
import java.io.IOException
import java.net.ConnectException
import java.net.Socket
import java.net.SocketException
import java.util.function.Consumer

class NetworkController(
		private val gameData: GameData,
		// not intended to set from outside
		private val observable: ObservableI = ObservableI()
) : Observable by observable {

	private val config = gameData.config
	private val logger = LogManager.getLogger(javaClass)!!

	var regionConnectionLost: (() -> Unit)? = null
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
					data = regionConnection.inp.readObject()
					logger.info("received ${data::class.simpleName}: $data")
					if (data is ClientListMsg) {
						this.gameData.playerData.playerList = data.players
					} else if (data is LobbyListMsg) {
						this.gameData.lobbyData.lobbyList = data.lobbies
					} else {
						val handled = this.observable.notifySubscriber(data)
						if (!handled) {
							logger.info("unknown msg from region: ${data::class.simpleName} $data")
						}
					}
				} while (true)
			} catch (e: SocketException) {
				logger.info("Region connection lost: $e")
				this.observable.notifySubscriber(RegionConnectionLost())
				regionConnectionLost?.invoke()
			} catch (e: EOFException) {
				logger.info("Region connection ended")
				this.observable.notifySubscriber(RegionConnectionLost())
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
				regionConnection.writeObject(ClientLogin(gameData.settings.name!!, this.tokens.removeAt(0)))
				this.listenToRegionConnection(regionConnection)
				connected = true
			} catch (e: Exception) {
				when (e) {
					is IOException, is ConnectException -> {
						for (i in 3 downTo 1) {
							gameData.loadingData.current = listOf("Connection to region server failed", "Trying again in ${i}s...")
							Thread.sleep(1000)
						}
					}
					else -> throw e
				}
			}
		} while (!connected)
	}

	fun createLobby(lobbyName: String, mode: String, consumer: Consumer<Pair<Lobby?, String?>>) {
		val msg = LobbyCreateMsg(lobbyName, mode)
		if (this.regionConnection == null)
			throw RuntimeException("No connection to region server")
		this.observable.subscribe(true, {
			when (it) {
				is Lobby -> consumer.accept(Pair(it, null))
				is LobbyNotCreatedMsg -> consumer.accept(Pair(null, it.msg))
				is RegionConnectionLost -> consumer.accept(Pair(null, "Region connection lost"))
			}
		}, LobbyNotCreatedMsg::class.java, Lobby::class.java, RegionConnectionLost::class.java)
		this.regionConnection!!.writeObject(msg)
	}

}