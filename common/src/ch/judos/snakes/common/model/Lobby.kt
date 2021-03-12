package ch.judos.snakes.common.model

class Lobby(
		val lobbyName: String,
		val lobbyId: String,
		val server: String,
		val serverPort: Int
){

	var players: List<String> = listOf()
	var mode: String = ""

	override fun toString(): String {
		return "${this.lobbyName} (${players.size})"
	}
}