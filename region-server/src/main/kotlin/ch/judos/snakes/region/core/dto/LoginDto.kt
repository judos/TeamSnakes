package ch.judos.snakes.region.core.dto

import ch.judos.snakes.region.core.model.enums.EUserRole


open class LoginDto {

	var username: String? = null
	var roles: List<EUserRole> = listOf()
	var loggedIn: Boolean = false

}
