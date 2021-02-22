package ch.judos.snakes.common.dto


class AuthSuccessDto() {

	lateinit var jwt: String

	constructor(jwt: String) : this() {
		this.jwt = jwt
	}

}
