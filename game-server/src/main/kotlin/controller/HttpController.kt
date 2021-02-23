package controller

import model.configuration.AppConfig
import org.glassfish.jersey.client.ClientConfig
import javax.ws.rs.client.Client
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.Entity
import javax.ws.rs.client.WebTarget
import javax.ws.rs.core.MediaType

class HttpController(
	private val appConfig: AppConfig
) {

	private var target: WebTarget

	init {
		val client: Client = ClientBuilder.newClient(ClientConfig())
		this.target = client.target(appConfig.region.url)!!
	}

	fun <T> post(path: String, obj: Any, returnType: Class<T>): T {
		val response = target.path(path).request()
			.accept(MediaType.APPLICATION_JSON)
			.post(Entity.json(obj))
		if (response.status / 100 != 2) {
			throw RuntimeException("request failed " + response.status)
		}
		return response.readEntity(returnType)
	}


}
