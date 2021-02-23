package model.configuration

import com.fasterxml.jackson.databind.ObjectMapper

class AppConfig {

	lateinit var server: ServerConfig

	lateinit var region: RegionConfig

	override fun toString(): String {
		val mapper = ObjectMapper()
		return mapper.writeValueAsString(this)
	}
}
