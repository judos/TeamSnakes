import ch.judos.snakes.common.controller.HttpController
import ch.judos.snakes.common.messages.game.RegionLogin
import ch.judos.snakes.common.model.Connection
import ch.judos.snakes.common.service.RandomService
import configuration.AppConfig
import controller.GameController
import controller.LobbyController
import controller.RegionController
import org.apache.logging.log4j.LogManager
import java.net.ServerSocket
import kotlin.system.exitProcess


class GameServerLauncher() {

	private val logger = LogManager.getLogger(javaClass)!!

	private var game: GameController
	private var random: RandomService
	private var lobby: LobbyController
	private var region: RegionController
	private var http: HttpController
	private var config: AppConfig
	private val connections = hashSetOf<Connection>()

	private var running = true

	init {
		this.config = AppConfig.load()
		this.random = RandomService()
		this.http = HttpController(this.config.region.url)
		this.game = GameController()
		this.lobby = LobbyController()
		this.region = RegionController(http, this.config, random, game, lobby)
		this.lobby.region = this.region

		this.acceptIncomingConnections()

		var failedConnectingAttempts = 0
		while (running) {
			if (!region.isConnected()) {
				region.register()
				for (i in 1 until 5) {
					Thread.sleep(1000)
					if (region.isConnected()) {
						failedConnectingAttempts = 0
						break
					}
				}
				if (!region.isConnected()) {
					failedConnectingAttempts++
					if (failedConnectingAttempts >= 12) {
						logger.error("Failed to connect to region server")
						exitProcess(1)
					}
				}
			}
			if (region.isConnected()) {
				Thread.sleep(5000)
				this.lobby.cleanup()
			}
		}
	}

	fun acceptIncomingConnections() {
		val socket = ServerSocket(this.config.server.port)
		val listenThread = Thread({
			while (true) {
				val connectionSocket = socket.accept()!!

				val connection = Connection(connectionSocket, connections::remove)
				connections.add(connection)
				val hello = connection.inp.readObject()
				if (hello is RegionLogin) {
					this.region.acceptConnection(connection, hello)
//		} else if (hello.startsWith("lobby")) {
//			this.lobby.acceptConnection(connection, hello)
				} else {
					logger.error("Invalid hello obj from connection: $hello")
					connection.close()
				}
			}
		}, "Connection Listener")
		listenThread.isDaemon = true
		listenThread.start()
	}

}


fun main() {
	GameServerLauncher()
}
