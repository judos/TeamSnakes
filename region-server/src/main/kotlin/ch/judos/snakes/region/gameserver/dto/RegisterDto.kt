package ch.judos.snakes.region.gameserver.dto


class RegisterDto {

	val url: String = ""
	val gameModes: List<String> = listOf()
	val currentLoad: Double = 0.0
	val lobbies: List<LobbyDto> = listOf()
}
