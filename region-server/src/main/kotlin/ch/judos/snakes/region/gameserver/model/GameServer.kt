package ch.judos.snakes.region.gameserver.model

import ch.judos.snakes.region.gameserver.dto.LobbyDto
import java.time.Duration
import java.time.LocalDateTime

class GameServer(
	var url: String, var gameModes: List<String>, var currentLoad: Double, val serverNr: Int,
	var lobbies: List<LobbyDto>
) {

	var lastUpdate: LocalDateTime = LocalDateTime.now()

	fun update(url: String, gameModes: List<String>, currentLoad: Double, lobbies: List<LobbyDto>) {
		this.url = url
		this.gameModes = gameModes
		this.currentLoad = currentLoad
		this.lobbies = lobbies
		this.lastUpdate = LocalDateTime.now()
	}

	fun isOlderThanS(seconds: Int): Boolean {
		return Duration.between(LocalDateTime.now(), this.lastUpdate).seconds >= seconds
	}
}
