package ch.judos.snakes.common.model

import java.io.*
import java.net.Socket

class Connection(
	socket: Socket,
	private val close: (Connection) -> Unit
) : Closeable {

	private var out: ObjectOutputStream
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

	fun writeObject(obj: Any) {
		this.out.writeObject(obj)
		this.out.reset()
		this.out.flush()
	}

}
