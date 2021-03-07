package ch.judos.snakes.client.model

import java.util.function.Consumer

class LoadingData {

	private var subscribers = mutableListOf<Consumer<LoadingData>>()


	var current: String = ""
		set(value) {
			field = value
			this.subscribers.forEach { it.accept(this) }
		}

	fun subscribe(subscriber: Consumer<LoadingData>) {
		this.subscribers.add(subscriber)
	}

	fun unsubscribe(subscriber: Consumer<LoadingData>) {
		this.subscribers.remove(subscriber)
	}


}