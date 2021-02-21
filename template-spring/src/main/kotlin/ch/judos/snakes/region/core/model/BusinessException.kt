package ch.judos.snakes.region.core.model

import ch.judos.snakes.region.core.dto.ErrorDto

class BusinessException constructor(
	val key: String,
	override val message: String,
	val details: Any? = null
) : RuntimeException(message) {

	fun toErrorDto(): ErrorDto {
		return ErrorDto(key, message, details)
	}

}
