import controller.AppProperties
import controller.HttpController
import controller.RegionController
import org.apache.logging.log4j.LogManager


class GameServerLauncher() {

	private val logger = LogManager.getLogger(javaClass)!!

	init {
		val properties = AppProperties()
		val http = HttpController(properties.config)
		val region = RegionController(http, properties.config)
		region.register()
	}

}


fun main() {
	GameServerLauncher()
}
