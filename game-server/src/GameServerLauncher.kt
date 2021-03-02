import ch.judos.snakes.common.messages.game.RegionLogin
import ch.judos.snakes.common.model.Connection
import ch.judos.snakes.common.service.RandomService
import configuration.AppConfig
import controller.GameController
import controller.HttpController
import controller.LobbyController
import controller.RegionController
import org.apache.logging.log4j.LogManager
import java.net.ServerSocket
import java.net.Socket
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
		this.http = HttpController(this.config)
		this.game = GameController()
		this.lobby = LobbyController()
		this.region = RegionController(http, this.config, random, game)

		this.acceptIncomingConnections()

		var failedConnectingAttempts = 0
		var lastLog = System.currentTimeMillis()
		while (running) {
			if (!region.isConnected()) {
				logger.info("Not connected to region")
				region.register()
				for (i in 1 until 30) {
					Thread.sleep(1000)
					if (region.isConnected()) {
						failedConnectingAttempts = 0
						break
					}
				}
				if (!region.isConnected()) {
					failedConnectingAttempts++
					if (failedConnectingAttempts >= 5) {
						logger.error("Failed to connect to region server")
						exitProcess(1)
					}
				}
			}
			if (region.isConnected()) {
				if (System.currentTimeMillis() - lastLog > 60 * 1000) {
					logger.info("Connected, lobby state: $lobby")
					lastLog = System.currentTimeMillis()
				}
				region.reportServerStats(lobby)
				Thread.sleep(5000)
			}
		}
		shutdown()
	}

	private fun shutdown() {

	}

	fun acceptIncomingConnections() {
		val socket = ServerSocket(this.config.server.port)
		val listenThread = Thread({
			while (true) {
				this.acceptConnection(socket.accept()!!)
			}
		}, "Connection Listener")
		listenThread.isDaemon = true
		listenThread.start()
	}

	private fun acceptConnection(socket: Socket) {
		val connection = Connection(socket, connections::remove)
		connections.add(connection)
		val hello = connection.inp.readUnshared()
				?: return logger.warn("Connection dropped before identified")
		if (hello is RegionLogin) {
			this.region.acceptConnection(connection, hello)
//		} else if (hello.startsWith("lobby")) {
//			this.lobby.acceptConnection(connection, hello)
		} else {
			logger.error("Invalid hello obj from connection: $hello")
			connection.close()
		}
	}

}


fun main() {
	GameServerLauncher()
}
