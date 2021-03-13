package ch.judos.snakes.common.messages.client

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.Serializable

class LobbyNotCreatedMsg(
		val msg: String
) : Serializable {

	override fun toString(): String {
		val mapper = ObjectMapper()
		return mapper.writeValueAsString(this)
	}
}