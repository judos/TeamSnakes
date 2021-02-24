package controller

import ch.judos.snakes.common.messages.region.LobbyInfo
import ch.judos.snakes.common.model.Connection

class LobbyController {

	fun acceptConnection(connection: Connection, hello: String) {
		// TODO: implement
	}

	override fun toString(): String {
		return "0 lobbies"
	}

	fun getLobbiesInfo(): List<LobbyInfo> {
		return listOf()
	}

}
