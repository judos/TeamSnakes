package ch.judos.snakes.region.core.dto

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.ResponseEntity
import javax.servlet.http.HttpServletResponse

class ErrorDto constructor(
	var key: String,
	var message: String,
	var details: Any? = null,
	@Transient val code: Int? = null
) {

	constructor(key: String, message: String, details: Any?) : this(
		key, message, details, null)

	fun toResponse(): ResponseEntity<Any> {
		return ResponseEntity.status(code ?: 400).body(this)
	}

	fun writeToResponse(response: HttpServletResponse) {
		response.status = code ?: 400
		response.writer.write(ObjectMapper().writeValueAsString(this))
	}

	companion object {
		fun invalidJwt(msg: String): ErrorDto {
			return ErrorDto("INVALID_JWT", msg, null, 401)
		}

		val missingRole = ErrorDto("MISSING_ROLE", "You are missing the required role", null, 403)
		val tooManyAttempts =
			ErrorDto("TOO_MANY_ATTEMPTS", "You are blocked due to too many recent attempts")
		val invalidCredentials = ErrorDto("INVALID_CREDENTIALS", "Provided credentials are invalid")
		val disabledUser = ErrorDto("DISABLED_USER", "User is disabled")
		val notLoggedIn =
			ErrorDto("NOT_LOGGED_IN", "You must be logged to execute this request", null, 401)

	}
}
