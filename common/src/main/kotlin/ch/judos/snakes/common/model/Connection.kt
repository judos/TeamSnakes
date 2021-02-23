package ch.judos.snakes.common.model

import java.io.BufferedReader
import java.io.Closeable
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class Connection(
	socket: Socket,
	private val close: (Connection) -> Unit
) : Closeable {

	var out: PrintWriter
	var inp: BufferedReader

	init {
		this.out = PrintWriter(socket.getOutputStream(), true)
		this.inp = BufferedReader(InputStreamReader(socket.getInputStream()))
	}

	override fun close() {
		this.out.close()
		this.inp.close()
		this.close.run { this }
	}
}
