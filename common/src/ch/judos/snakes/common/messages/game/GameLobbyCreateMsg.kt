package ch.judos.snakes.common.messages.game

import ch.judos.snakes.common.messages.region.LobbyCreateMsg

class GameLobbyCreateMsg(
		name: String,
		mode: String,
		var lobbyId: String
) : LobbyCreateMsg(name, mode) {
}