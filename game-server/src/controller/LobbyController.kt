package controller

import ch.judos.snakes.common.messages.game.GameLobbyCreateMsg
import ch.judos.snakes.common.model.Connection
import ch.judos.snakes.common.model.Lobby

class LobbyController(
) {

	val lobbies = mutableListOf<Lobby>()

	fun acceptConnection(connection: Connection, hello: String) {
		// TODO: implement
	}

	override fun toString(): String {
		return "${this.lobbies.size} lobbies"
	}

	fun getLobbiesInfo(): List<Lobby> {
		return this.lobbies
	}

	fun createLobby(data: GameLobbyCreateMsg) {
		// TODO: remove server/port from this model
		val lobby = Lobby(data.name, data.lobbyId, "", 0)
		this.lobbies.add(lobby)
	}

}
