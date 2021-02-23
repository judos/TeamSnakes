package ch.judos.snakes.region.gameserver.model

import ch.judos.snakes.region.gameserver.dto.LobbyDto
import java.time.Duration
import java.time.LocalDateTime

class GameServer(
	var host: String,
	var port: Int,
	var gameModes: List<String>,
	val serverNr: Int,
) {
	var lobbies: List<LobbyDto> = listOf()
	var currentLoad: Double = 1.0

	var lastUpdate: LocalDateTime = LocalDateTime.now()

	fun update(host: String, gameModes: List<String>, currentLoad: Double, lobbies: List<LobbyDto>) {
		this.host = host
		this.gameModes = gameModes
		this.currentLoad = currentLoad
		this.lobbies = lobbies
		this.lastUpdate = LocalDateTime.now()
	}

	fun isOlderThanS(seconds: Int): Boolean {
		return Duration.between(this.lastUpdate, LocalDateTime.now()).seconds >= seconds
	}

	override fun toString(): String {
		return this.host + " " + this.gameModes + " lobbies: " + this.lobbies.size
	}
}
