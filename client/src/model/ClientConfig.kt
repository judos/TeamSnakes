package model

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper
import org.apache.logging.log4j.LogManager
import java.io.FileReader
import java.util.*

class ClientConfig {

	lateinit var region: RegionConfig


	override fun toString(): String {
		val mapper = ObjectMapper()
		return mapper.writeValueAsString(this)
	}

	companion object {
		private val logger = LogManager.getLogger(this::class.java)!!

		fun load(): ClientConfig {
			val mapper = JavaPropsMapper()

			val properties = Properties()
			val file = ClientConfig::class.java.getResource("/application.properties")
			val fileReader = FileReader(file.file)
			properties.load(fileReader)

			val config = mapper.readPropertiesAs(properties, ClientConfig::class.java)
			logger.info(config)
			return config
		}
	}
}
