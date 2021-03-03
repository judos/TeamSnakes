package ch.judos.snakes.common.dto


class UserAuthSuccessDto(
		jwt: String,
		val tokens: MutableList<String>
) : AuthSuccessDto(jwt) {

	constructor() : this("", mutableListOf())

}
