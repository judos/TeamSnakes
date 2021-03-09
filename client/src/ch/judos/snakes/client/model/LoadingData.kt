package ch.judos.snakes.client.model

import java.util.function.Consumer

class LoadingData {

	var subscribers = mutableListOf<Consumer<LoadingData>>()

	var current: List<String> = listOf()
		set(value) {
			field = value
			this.subscribers.forEach { it.accept(this) }
		}

	fun set(current: String) {
		this.current = listOf(current)
	}

}