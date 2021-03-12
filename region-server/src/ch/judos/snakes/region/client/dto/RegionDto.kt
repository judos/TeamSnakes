package ch.judos.snakes.region.client.dto

import ch.judos.snakes.common.messages.region.LobbyCreateMsg

class RegionDto constructor(
	val regionName: String,
	val otherRegions: List<String>,
	val gameModes: List<String>,
	val lobbies: Collection<LobbyCreateMsg>
)
