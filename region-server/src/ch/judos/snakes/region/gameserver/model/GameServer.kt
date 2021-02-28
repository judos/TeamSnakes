package ch.judos.snakes.region.gameserver.model

import ch.judos.snakes.common.messages.region.LobbyInfo
import java.time.Duration
import java.time.LocalDateTime

class GameServer(
		val name: String,
		val host: String,
		val port: Int,
		val gameModes: List<String>,
		val serverNr: Int,
) {
	var lobbies: List<LobbyInfo> = listOf()
	var currentLoad: Double = 1.0

	var lastUpdate: LocalDateTime = LocalDateTime.now()

	fun update(currentLoad: Double, lobbies: List<LobbyInfo>) {
		this.currentLoad = currentLoad
		this.lobbies = lobbies
		this.lastUpdate = LocalDateTime.now()
	}

	fun isOlderThanS(seconds: Int): Boolean {
		return Duration.between(this.lastUpdate, LocalDateTime.now()).seconds >= seconds
	}

	override fun toString(): String {
		return this.name + " [" + this.gameModes + "]"
	}
}
