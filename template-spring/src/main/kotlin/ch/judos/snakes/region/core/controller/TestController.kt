package ch.judos.snakes.region.core.controller

import ch.judos.snakes.region.core.dto.EncodingDto
import ch.judos.snakes.region.core.dto.LoginDto
import ch.judos.snakes.region.core.model.enums.EUserRole
import ch.judos.snakes.region.core.services.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("test", produces = ["application/json"])
class TestController @Autowired constructor(
		private val passwordEncoder: PasswordEncoder,
		private val authService: AuthService
) {

	private val logger = LoggerFactory.getLogger(javaClass)!!


	@GetMapping(path = ["/mylogin"])
	@Operation(summary = "Check if you're logged in and what roles are available for your login")
	@SecurityRequirement(name = "jwt_auth")
	fun getMylogin(): LoginDto {
		val user: UserDetails? = this.authService.getAdminUser()
		val result = LoginDto()
		if (user != null) {
			result.username = user.username
			result.roles = user.authorities.map { EUserRole.valueOf(it.authority) }
			result.loggedIn = true
		}
		return result
	}

	@GetMapping("/encode/{pw}")
	fun encodePassword(@PathVariable pw: String): EncodingDto {
		val encoded = this.passwordEncoder.encode(pw)
		if (!this.passwordEncoder.matches(pw, encoded)) {
			throw RuntimeException("PW and encoded PW do not match!")
		}
		return EncodingDto(pw, encoded, this.passwordEncoder.toString())
	}


}
