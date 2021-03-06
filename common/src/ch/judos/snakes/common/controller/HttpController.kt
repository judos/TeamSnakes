package ch.judos.snakes.common.controller

import com.fasterxml.jackson.databind.ObjectMapper
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandlers


class HttpController(
		private val url: String
) {

	private var client: HttpClient
	var jwt: String? = null

	init {
		this.client = HttpClient.newHttpClient()!!
	}

	fun <T> post(path: String, obj: Any, returnType: Class<T>): T {
		val objectMapper = ObjectMapper()
		val str = objectMapper.writeValueAsString(obj)
		val data = BodyPublishers.ofString(str)

		// create a request
		var builder = HttpRequest.newBuilder(URI.create("$url/$path"))
				.header("Content-Type", "application/json")
				.header("accept", "application/json")
		if (jwt != null) {
			builder = builder.header("Authorization", "Bearer $jwt")
		}
		val request = builder.POST(data)
				.build()!!
		// use the client to send the request
		val response = client.send(request, BodyHandlers.ofString())
		if (response.statusCode() / 100 != 2) {
			val error = response.body()
			throw RuntimeException("request failed ${response.statusCode()} returned: $error")
		}
		val responseStr = response.body()
		return objectMapper.readValue(responseStr, returnType)
	}


}
