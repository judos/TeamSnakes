package ch.judos.snakes.common.model

import java.io.Serializable

class Lobby(
		val lobbyName: String,
		val lobbyId: String,
		val server: String,
		val serverPort: Int
) : Serializable {

	var players: List<String> = listOf()
	var mode: String = ""

	override fun toString(): String {
		return "${this.lobbyName} (${players.size})"
	}
}