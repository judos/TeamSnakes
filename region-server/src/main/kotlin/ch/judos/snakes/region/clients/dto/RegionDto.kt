package ch.judos.snakes.region.clients.dto

import ch.judos.snakes.region.gameserver.dto.LobbyDto

class RegionDto constructor(
	val regionName: String,
	val otherRegions: List<String>,
	val gameModes: List<String>,
	val lobbies: Collection<LobbyDto>
)
