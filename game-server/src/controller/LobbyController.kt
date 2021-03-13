package controller

import ch.judos.snakes.common.messages.game.GameLobbyCreateMsg
import ch.judos.snakes.common.model.Connection
import ch.judos.snakes.common.model.Lobby
import org.apache.logging.log4j.LogManager

class LobbyController {

	private val logger = LogManager.getLogger(javaClass)!!

	var region: RegionController? = null

	val lobbies = mutableListOf<Lobby>()

	fun acceptConnection(connection: Connection, hello: String) {
		// TODO: implement
	}

	override fun toString(): String {
		return "${this.lobbies.size} lobbies: ${this.lobbies}"
	}

	fun getLobbiesInfo(): List<Lobby> {
		return this.lobbies
	}

	fun createLobby(data: GameLobbyCreateMsg) {
		logger.info("Create lobby ${data.name}")
		// TODO: remove server/port from this model
		val lobby = Lobby(data.name, data.lobbyId, "", 0)
		this.lobbies.add(lobby)
	}

	fun cleanup() {
		val now = System.currentTimeMillis()
		val timeout = 30 * 1000
		val updated = this.lobbies.removeAll { it.players.size == 0 && now - it.created > timeout }
		if (updated) {
			this.region!!.reportServerStats()
		}
	}

}
