package model.configuration

import com.fasterxml.jackson.annotation.JsonIgnore
import dto.LoginRequestDto

class RegionConfig {

	lateinit var url: String
	lateinit var username: String
	lateinit var password: String

	@JsonIgnore
	fun getLoginRequest(): LoginRequestDto {
		return LoginRequestDto(username, password)
	}
}
