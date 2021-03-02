package ch.judos.snakes.common.dto

class GameserverConnectDto() {

	lateinit var token: String
	lateinit var host: String
	var port: Int = 0
	lateinit var gameModes: List<String>

	constructor(gameserverToken: String, host: String, port: Int, gameModes: List<String>) : this() {
		this.token = gameserverToken
		this.host = host
		this.port = port
		this.gameModes = gameModes
	}
}
