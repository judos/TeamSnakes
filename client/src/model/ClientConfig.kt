package model

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

class ClientConfig {

	private var file: File
	private var properties: Properties

	init {
		this.properties = Properties()
		this.file = File("client-config.txt")
		if (file.exists()) {
			val fis = FileInputStream(file)
			this.properties.load(fis)
		}
	}


	var name: String?
		get() = this.properties["name"] as String?
		set(name: String?) {
			this.properties.setProperty("name", name)
			saveToFile()
		}

	private fun saveToFile() {
		val fos = FileOutputStream(this.file)
		this.properties.store(fos, "Basic Configuration")
		fos.close()
	}
}