package ch.judos.snakes.common.messages.client

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.Serializable

class ClientListMsg(
		val players: List<String>
) : Serializable {



	override fun toString(): String {
		val mapper = ObjectMapper()
		return mapper.writeValueAsString(this)
	}
}