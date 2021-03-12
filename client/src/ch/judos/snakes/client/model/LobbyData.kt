package ch.judos.snakes.client.model

import ch.judos.snakes.common.model.Lobby
import java.util.function.Consumer

class LobbyData {

	val subscribers = mutableListOf<Consumer<LobbyData>>()

	var lobbyList: List<Lobby> = listOf()
		set(value) {
			field = value
			subscribers.forEach { it.accept(this) }
		}

}
