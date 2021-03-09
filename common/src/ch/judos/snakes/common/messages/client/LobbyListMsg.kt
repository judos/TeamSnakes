package ch.judos.snakes.common.messages.client

import ch.judos.snakes.common.model.Lobby
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.Serializable

class LobbyListMsg(
		val lobbies: List<Lobby>
) : Serializable {


	override fun toString(): String {
		val mapper = ObjectMapper()
		return mapper.writeValueAsString(this)
	}
}