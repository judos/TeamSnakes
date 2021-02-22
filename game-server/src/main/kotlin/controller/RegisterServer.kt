package controller

import model.configuration.Configuration
import org.apache.logging.log4j.LogManager
import org.glassfish.jersey.client.ClientConfig
import javax.ws.rs.client.Client
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.Entity
import javax.ws.rs.core.Form
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response


class RegisterServer(configuration: Configuration) {

	private val logger = LogManager.getLogger(javaClass)!!

	init {
		val client: Client = ClientBuilder.newClient(ClientConfig())
		val target = client.target("http://localhost:8080")
		val data = configuration.server.region.getLoginRequest()

		val response = target.path("authenticate").request()
			.accept(MediaType.APPLICATION_JSON)
			.post(Entity.json(data))

		logger.info(response)
		logger.info(response.status)
		logger.info(response.entity)
	}


}
