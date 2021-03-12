package ch.judos.snakes.region.gameserver.model

import ch.judos.snakes.common.model.Connection
import ch.judos.snakes.common.model.Lobby
import java.time.Duration
import java.time.LocalDateTime

class GameServer(
		val name: String,
		val host: String,
		val port: Int,
		val gameModes: List<String>,
		val serverNr: Int,
) {
	var lobbies: List<Lobby> = listOf()
	var currentLoad: Double = 1.0
	var connection: Connection? = null

	var lastUpdate: LocalDateTime = LocalDateTime.now()

	fun update(currentLoad: Double, lobbies: List<Lobby>) {
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
