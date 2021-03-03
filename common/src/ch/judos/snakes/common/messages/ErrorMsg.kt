package ch.judos.snakes.common.messages

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.Serializable

class ErrorMsg(
		var code: String,
		var msg: String,
) : Serializable {


	override fun toString(): String {
		val mapper = ObjectMapper()
		return mapper.writeValueAsString(this)
	}
}
