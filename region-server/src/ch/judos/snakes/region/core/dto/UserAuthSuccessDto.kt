package ch.judos.snakes.region.core.dto


class UserAuthSuccessDto(
		jwt: String,
		val tokens: List<String>
) : AuthSuccessDto(jwt) {

}
