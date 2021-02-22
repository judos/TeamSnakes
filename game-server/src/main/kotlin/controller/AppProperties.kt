package controller

import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper
import model.configuration.AppConfig
import org.apache.logging.log4j.LogManager
import java.io.FileReader
import java.util.*

class AppProperties {

	var config: AppConfig
	private val logger = LogManager.getLogger(javaClass)!!

	init {
		val mapper = JavaPropsMapper()

		val properties = Properties()
		val file = javaClass.getResource("/application.properties")
		val fileReader = FileReader(file.file)
		properties.load(fileReader)

		this.config = mapper.readPropertiesAs(properties, AppConfig::class.java)
		logger.info(this.config)
	}
}
