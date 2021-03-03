package ch.judos.snakes.region.core.controller

import ch.judos.snakes.common.dto.AuthSuccessDto
import ch.judos.snakes.common.dto.GuestLoginRequestDto
import ch.judos.snakes.common.dto.UserAuthSuccessDto
import ch.judos.snakes.common.service.RandomService
import ch.judos.snakes.region.core.config.auth.JwtTokenUtil
import ch.judos.snakes.region.core.dto.*
import ch.judos.snakes.region.core.entity.AdminUser
import ch.judos.snakes.region.core.entity.QUserToken
import ch.judos.snakes.region.core.entity.UserToken
import ch.judos.snakes.region.core.model.enums.ELoginType
import ch.judos.snakes.region.core.repository.AdminUserRepository
import ch.judos.snakes.region.core.repository.UserTokenRepository
import ch.judos.snakes.region.core.service.LoginAttemptService
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime


@RestController
@RequestMapping("/authenticate", produces = ["application/json"])
class AuthenticationController @Autowired constructor(
		private val authenticationManager: AuthenticationManager,
		private val adminUserRepository: AdminUserRepository,
		private val jwtTokenUtil: JwtTokenUtil,
		private val loginAttemptService: LoginAttemptService,
		private val userTokenRepository: UserTokenRepository,
		private val randomService: RandomService
) {

	private val logger = LoggerFactory.getLogger(javaClass)!!

	@PostMapping(path = [""])
	@ApiResponses(value = [
		ApiResponse(responseCode = "200",
				content = [Content(schema = Schema(implementation = AuthSuccessDto::class))]),
		ApiResponse(responseCode = "400",
				description = "error key may be one of: TOO_MANY_ATTEMPTS, DISABLED_USER, INVALID_CREDENTIALS",
				content = [Content(schema = Schema(implementation = ErrorDto::class))])
	])
	fun authenticate(@RequestBody request: LoginRequestDto): ResponseEntity<Any> {
		if (this.loginAttemptService.isBlocked(ELoginType.USER, request.username)) {
			return ErrorDto.tooManyAttempts.toResponse()
		}
		return try {
			val authentication = authenticationManager.authenticate(
					UsernamePasswordAuthenticationToken(request.username, request.password))
			val adminUser = authentication.principal as AdminUser
			adminUser.lastActive = LocalDateTime.now()
			this.adminUserRepository.save(adminUser)
			val token = jwtTokenUtil.generateUserToken(adminUser)
			ResponseEntity.ok(AuthSuccessDto(token))
		} catch (e: DisabledException) {
			return ErrorDto.disabledUser.toResponse()
		} catch (e: LockedException) {
			return ErrorDto.disabledUser.toResponse()
		} catch (e: BadCredentialsException) {
			return ErrorDto.invalidCredentials.toResponse()
		}
	}


	@PostMapping(path = ["guest"])
	@ApiResponses(value = [
		ApiResponse(responseCode = "200",
				content = [Content(schema = Schema(implementation = AuthSuccessDto::class))]),
		ApiResponse(responseCode = "400",
				description = "error key may be one of: _",
				content = [Content(schema = Schema(implementation = ErrorDto::class))])
	])
	fun authGuest(@RequestBody request: GuestLoginRequestDto): ResponseEntity<Any> {
		val jwt = this.jwtTokenUtil.generateGuestToken(request.username)
		val oldTokens = this.userTokenRepository.findAll(QUserToken.userToken.username.eq(request.username))
		this.userTokenRepository.deleteAll(oldTokens)
		val tokens = this.randomService.generateTokens(5, 64)
		tokens.forEach {
			this.userTokenRepository.save(UserToken().apply {
				this.username = request.username
				this.token = it
			})
		}
		return ResponseEntity.ok(UserAuthSuccessDto(jwt, tokens))
	}


}
