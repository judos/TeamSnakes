package ch.judos.snakes.region.clients.controller

import ch.judos.snakes.region.clients.dto.RegionDto
import ch.judos.snakes.region.core.config.AppProperties
import ch.judos.snakes.region.gameserver.services.GameServerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/clients", produces = ["application/json"])
class ClientController @Autowired constructor(
	private val gameServerService: GameServerService,
	private val appProperties: AppProperties
) {

	@GetMapping(path = [""])
	fun getRegion(): ResponseEntity<RegionDto> {
		return ResponseEntity.ok(RegionDto(appProperties.regionName, appProperties.otherRegions, this.gameServerService.gameModes(), this.gameServerService.lobbies.values))
	}
}
