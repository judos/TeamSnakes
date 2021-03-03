package ch.judos.snakes.common.messages.region

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.Serializable

class GameUpdate(
	val currentLoad: Double,
	var lobbies: List<LobbyInfo>
) : Serializable {


	override fun toString(): String {
		val mapper = ObjectMapper()
		return mapper.writeValueAsString(this)
	}

}
