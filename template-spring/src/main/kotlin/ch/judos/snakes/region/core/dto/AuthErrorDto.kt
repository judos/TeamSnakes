package ch.judos.snakes.region.core.dto

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.ResponseEntity
import javax.servlet.http.HttpServletResponse


enum class EAuthError {
	INVALID_JWT, EXPIRED_JWT, NOT_LOGGED_IN, MISSING_ROLE
}


// TODO: remove class use ErrorDto
class AuthErrorDto(
		var key: EAuthError,
		var message: String
) {


	companion object {
		fun jwtError(response: HttpServletResponse, status: Int, code: EAuthError, message: String) {
			response.status = status
			val error = AuthErrorDto(code, message)
			response.writer.write(ObjectMapper().writeValueAsString(error))
		}

		fun notLoggedIn(): ResponseEntity<AuthErrorDto> {
			return ResponseEntity.status(401)
					.body(AuthErrorDto(EAuthError.NOT_LOGGED_IN, "You must be logged to execute this request"))
		}
	}
}
