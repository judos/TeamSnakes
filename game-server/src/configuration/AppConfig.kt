package configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper
import org.apache.logging.log4j.LogManager
import java.io.FileReader
import java.util.*

class AppConfig {


	lateinit var server: ServerConfig

	lateinit var region: RegionConfig

	override fun toString(): String {
		val mapper = ObjectMapper()
		return mapper.writeValueAsString(this)
	}

	companion object {
		private val logger = LogManager.getLogger(this::class.java)!!

		fun load(): AppConfig {
			val mapper = JavaPropsMapper()

			val properties = Properties()
			val file = AppConfig::class.java.getResource("/application.properties")
			val fileReader = FileReader(file.file)
			properties.load(fileReader)

			val config = mapper.readPropertiesAs(properties, AppConfig::class.java)
			logger.info(config)
			return config
		}
	}
}
