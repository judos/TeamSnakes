package ch.judos.snakes.client.model

import java.util.function.Consumer

class PlayerData {

	val subscribers = mutableListOf<Consumer<PlayerData>>()

	var playerList: List<String> = listOf()
		set(value) {
			field = value
			subscribers.forEach { it.accept(this) }
		}
}