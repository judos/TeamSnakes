package model.configuration

import dto.LoginRequestDto

class RegionConfig {

	lateinit var url: String
	lateinit var username: String
	lateinit var password: String

	fun getLoginRequest(): LoginRequestDto {
		return LoginRequestDto(username, password)
	}
}
