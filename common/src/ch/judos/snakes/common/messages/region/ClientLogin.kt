package ch.judos.snakes.common.messages.region

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.Serializable

class ClientLogin(
		val username: String,
		val token: String
) : Serializable {


	override fun toString(): String {
		val mapper = ObjectMapper()
		return mapper.writeValueAsString(this)
	}

}