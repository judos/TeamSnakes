import controller.AppProperties
import controller.RegisterServer
import org.apache.logging.log4j.LogManager


class GameServerLauncher() {

	private val logger = LogManager.getLogger(javaClass)!!

	init {
		val properties = AppProperties()
		val register = RegisterServer(properties.configuration)
	}

}


fun main() {
	GameServerLauncher()
}
