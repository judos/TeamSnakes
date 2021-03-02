package controller

import configuration.AppConfig
import org.glassfish.jersey.client.ClientConfig
import java.util.*
import javax.ws.rs.client.Client
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.Entity
import javax.ws.rs.client.WebTarget
import javax.ws.rs.core.MediaType

class HttpController(
		private val appConfig: AppConfig
) {

	var jwt: String? = null
	private var target: WebTarget

	init {
		val client: Client = ClientBuilder.newClient(ClientConfig())
		this.target = client.target(appConfig.region.url)!!
	}

	fun <T> post(path: String, obj: Any, returnType: Class<T>): T {
		var builder = target.path(path).request()
				.accept(MediaType.APPLICATION_JSON)
		if (jwt != null) {
			builder = builder.header("Authorization", "Bearer $jwt")
		}
		val response = builder.post(Entity.json(obj))
		if (response.status / 100 != 2) {
			val error = response.readEntity(HashMap::class.java)
			throw RuntimeException("request failed ${response.status} returned: $error")
		}
		return response.readEntity(returnType)
	}


}
