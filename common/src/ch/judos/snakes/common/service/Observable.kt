package ch.judos.snakes.common.service

import java.util.function.Consumer

interface Observable {
	fun subscribe(single: Boolean, observer: Consumer<Any>, vararg msgs: Class<out Any>)
	fun unsubscribe(observer: Consumer<Any>)
}


open class ObservableI : Observable {

	var subscriber: MutableMap<Class<out Any>, MutableList<Pair<Consumer<Any>, Boolean>>> = mutableMapOf()

	override fun subscribe(single: Boolean, observer: Consumer<Any>, vararg msgs: Class<out Any>) {
		synchronized(this.subscriber) {
			for (msg in msgs) {
				var list = this.subscriber[msg]
				if (list == null) {
					list = mutableListOf()
					this.subscriber[msg] = list
				}
				list.add(Pair(observer, single))
			}
		}
	}

	override fun unsubscribe(observer: Consumer<Any>) {
		synchronized(this.subscriber) {
			for ((_, entry) in this.subscriber.entries) {
				entry.removeIf { it.first == observer }
			}
		}
	}

	fun notifySubscriber(msg: Any): Boolean {
		var list: List<Consumer<Any>>
		synchronized(this.subscriber) {
			val subscriber = this.subscriber[msg.javaClass] ?: return false
			list = subscriber.map { it.first }
			subscriber.filter { it.second }.forEach { unsubscribe(it.first) }
		}
		var handled = 0
		list.forEach { it.accept(msg); handled++ }
		return handled > 0
	}


}