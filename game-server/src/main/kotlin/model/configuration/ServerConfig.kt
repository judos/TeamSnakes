package model.configuration

class ServerConfig {

	var port: Int = 0
	lateinit var url: String
	lateinit var region: RegionConfig

	fun getServerAddress(): String {
		return "$url:$port"
	}

}
