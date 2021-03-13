package ch.judos.snakes.common.messages.region

import ch.judos.snakes.common.model.Lobby
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.Serializable

class LobbyUpdateList(
		val currentLoad: Double,
		var lobbies: List<Lobby>
) : Serializable {


	override fun toString(): String {
		val mapper = ObjectMapper()
		return mapper.writeValueAsString(this)
	}

}
