package ch.judos.snakes.common.messages.game

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.Serializable

class RegionLogin(
	val regionToken: String
) : Serializable {


	override fun toString(): String {
		val mapper = ObjectMapper()
		return mapper.writeValueAsString(this)
	}
}
