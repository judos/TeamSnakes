package controller

import jakarta.ws.rs.client.ClientBuilder
import jakarta.ws.rs.client.Entity
import jakarta.ws.rs.client.WebTarget
import jakarta.ws.rs.core.MediaType
import model.configuration.Configuration
import org.apache.logging.log4j.LogManager
import org.glassfish.jersey.client.ClientConfig


class RegisterServer(configuration: Configuration) {

	private val logger = LogManager.getLogger(javaClass)!!

	init {
		val config = ClientConfig()
		val client = ClientBuilder.newClient(config)
		val target: WebTarget = client.target(configuration.server.region.url).path("/authenticate")
		val builder = target.request(MediaType.APPLICATION_JSON)
		val response = builder.post(Entity.json(configuration.server.region.getLoginRequest()))
		logger.info(response)
		logger.info(response.status)
		logger.info(response.entity)
	}


}
