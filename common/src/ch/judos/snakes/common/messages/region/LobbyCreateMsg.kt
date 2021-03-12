package ch.judos.snakes.common.messages.region

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.Serializable

open class LobbyCreateMsg(
	var name: String,
	var mode: String,
) : Serializable {



	override fun toString(): String {
		val mapper = ObjectMapper()
		return mapper.writeValueAsString(this)
	}
}
