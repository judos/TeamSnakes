package ch.judos.snakes.common.model

import java.io.*
import java.net.Socket

class Connection(
	socket: Socket,
	private val close: (Connection) -> Unit
) : Closeable {

	var out: ObjectOutputStream
	var inp: ObjectInputStream

	init {
		this.out = ObjectOutputStream(socket.getOutputStream())
		this.inp = ObjectInputStream(socket.getInputStream())
	}

	override fun close() {
		this.out.close()
		this.inp.close()
		this.close.run { this }
	}
}
