package ch.judos.snakes.client.model

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

class ClientSettings {

	private var file: File
	private var properties: Properties

	init {
		this.properties = Properties()
		val folder = File("client")
		if (!folder.exists())
			folder.mkdir()
		this.file = File("client/client-config.txt")
		if (file.exists()) {
			val fis = FileInputStream(file)
			this.properties.load(fis)
		}
	}


	var name: String?
		get() = this.properties["name"] as String?
		set(name) {
			this.properties.setProperty("name", name)
			saveToFile()
		}


	private fun saveToFile() {
		val fos = FileOutputStream(this.file)
		this.properties.store(fos, "Basic Configuration")
		fos.close()
	}
}