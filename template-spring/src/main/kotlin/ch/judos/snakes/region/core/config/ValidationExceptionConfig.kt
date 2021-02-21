package ch.judos.snakes.region.core.config

import ch.judos.snakes.region.core.dto.ErrorDto
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*
import java.util.function.Consumer


@ControllerAdvice
class ValidationExceptionConfig : ResponseEntityExceptionHandler() {

	override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders,
			status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
		val errors: MutableMap<String, String?> = HashMap()
		ex.bindingResult.allErrors.forEach(Consumer { error: ObjectError ->
			val fieldName = (error as FieldError).field
			val errorMessage = error.getDefaultMessage()
			errors[fieldName] = errorMessage
		})
		return ResponseEntity.badRequest().body(ErrorDto("INVALID_REQUEST", ex.message, errors))
	}

	override fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException, headers: HttpHeaders,
			status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
		val msg =  ex.rootCause?.message ?: ex.message ?: ""
		return ResponseEntity.badRequest().body(ErrorDto("INVALID_REQUEST", msg))
	}

}
