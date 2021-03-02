package ch.judos.snakes.common.messages.region

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.Serializable

class LobbyInfo(
	var name: String,
	var mode: String,
	var players: List<String>
) : Serializable {




	override fun toString(): String {
		val mapper = ObjectMapper()
		return mapper.writeValueAsString(this)
	}
}
