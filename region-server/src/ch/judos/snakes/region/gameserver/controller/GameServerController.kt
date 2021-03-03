package ch.judos.snakes.region.gameserver.controller

import ch.judos.snakes.common.dto.GameserverConnectDto
import ch.judos.snakes.region.core.dto.ErrorDto
import ch.judos.snakes.region.core.service.AuthService
import ch.judos.snakes.region.gameserver.service.GameServerService
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
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
	private val logger = LoggerFactory.getLogger(javaClass)

	@PostMapping(path = ["connect"])
	@ApiResponses(
		value = [
			ApiResponse(responseCode = "200",
				content = [Content(schema = Schema(implementation = Int::class))],
				description = "Server number"),
			ApiResponse(
				responseCode = "401",
				description = "error key may be one of: NOT_LOGGED_IN",
				content = [Content(schema = Schema(implementation = ErrorDto::class))]
			)
		]
	)
	fun connect(@RequestBody request: GameserverConnectDto): ResponseEntity<Any> {
		val user = this.authService.getAdminUser() ?: return ErrorDto.notLoggedIn.toResponse()
		val serverNr = this.gameServerService.register(user, request)
		return ResponseEntity.ok(serverNr)
	}


}
