package ch.judos.snakes.region.core.dto

class ErrorDto constructor(
		var key: String,
		var message: String,
		var details: Any? = null
)
