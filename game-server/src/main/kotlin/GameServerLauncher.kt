import java.io.BufferedReader
import java.io.FileReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class GameServerLauncher() {
	init {
		val p = Properties()
		val fileReader = FileReader(javaClass.getResource("application.properties").file)
		p.load(fileReader)
		val propertyMap = HashMap<String, String>()
		val keys: Enumeration<*> = p.propertyNames()
		while (keys.hasMoreElements()) {
			val key = keys.nextElement() as String
			propertyMap[key] = p.getProperty(key)
		}
		fileReader.close()
		println(propertyMap)

		sendPostRequest("http://localhost:8080/gameserver/update", "{}")
	}

	fun sendPostRequest(requestUrl: String?, payload: String?): String? {
		return try {
			val url = URL(requestUrl)
			val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
			connection.setDoInput(true)
			connection.setDoOutput(true)
			connection.setRequestMethod("POST")
			connection.setRequestProperty("Accept", "application/json")
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
			val writer = OutputStreamWriter(connection.getOutputStream(), "UTF-8")
			writer.write(payload)
			writer.close()
			val br = BufferedReader(InputStreamReader(connection.getInputStream()))
			val jsonString = StringBuffer()
			var line: String?
			while (br.readLine().also { line = it } != null) {
				jsonString.append(line)
			}
			br.close()
			connection.disconnect()
			jsonString.toString()
		} catch (e: Exception) {
			throw RuntimeException(e.message)
		}
	}
}


fun main() {
	GameServerLauncher()
}
