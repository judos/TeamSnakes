package ch.judos.snakes.region.core.controller

import ch.judos.snakes.region.core.dto.ErrorDto
import ch.judos.snakes.region.core.model.BusinessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus


@ControllerAdvice
class ExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BusinessException::class)
	fun handleBusinessException(exception: BusinessException): ResponseEntity<ErrorDto> {
		return ResponseEntity.badRequest().body(exception.toErrorDto())
	}


}
