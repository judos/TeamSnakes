package configuration

import com.fasterxml.jackson.annotation.JsonIgnore

class ServerConfig {

	var port: Int = 0
	lateinit var url: String

	@JsonIgnore
	fun getServerAddress(): String {
		return "$url:$port"
	}

}
