package controller

import ch.judos.snakes.common.dto.AuthSuccessDto
import model.configuration.AppConfig
import org.apache.logging.log4j.LogManager


class RegionController(
	private val http: HttpController,
	private val config: AppConfig
) {
	private val logger = LogManager.getLogger(javaClass)!!

	private var jwt: String? = null

	fun register() {
		val data = config.server.region.getLoginRequest()
		try {
			val response = this.http.post("authenticate", data, AuthSuccessDto::class.java)
			this.jwt = response.jwt
			logger.info("Login to region successful")
		} catch (exception: Exception) {
			throw RuntimeException("Couldn't authenticate to region server")
		}
	}

}
