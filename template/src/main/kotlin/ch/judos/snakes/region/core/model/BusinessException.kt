package ch.judos.snakes.region.core.model

class BusinessException constructor(
		var key: String,
		override var message: String,
		var details: Any? = null
) : RuntimeException(message)
