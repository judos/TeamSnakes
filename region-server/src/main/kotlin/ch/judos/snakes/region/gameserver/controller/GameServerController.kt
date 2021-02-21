package ch.judos.snakes.region.gameserver.controller

import ch.judos.snakes.region.core.dto.ErrorDto
import ch.judos.snakes.region.core.services.AuthService
import ch.judos.snakes.region.gameserver.dto.RegisterDto
import ch.judos.snakes.region.gameserver.services.GameServerService
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/gameserver", produces = ["application/json"])
class GameServerController @Autowired constructor(
	private val gameServerService: GameServerService,
	private val authService: AuthService
) {

	@PostMapping(path = ["update"])
	@ApiResponses(
		value = [
//		ApiResponse(responseCode = "200",
//			content = [Content(schema = Schema(implementation = AuthSuccessDto::class))]),
			ApiResponse(
				responseCode = "400",
				description = "error key may be one of: NOT_LOGGED_IN",
				content = [Content(schema = Schema(implementation = ErrorDto::class))]
			)
		]
	)
	fun register(@RequestBody request: RegisterDto): ResponseEntity<Any> {
		val user = this.authService.getAdminUser() ?: return ErrorDto.notLoggedIn.toResponse()
		val serverNr = this.gameServerService.register(user, request)
		return ResponseEntity.ok(serverNr)
	}


}