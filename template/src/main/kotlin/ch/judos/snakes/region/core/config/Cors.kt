package ch.judos.snakes.region.core.config

import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter


@Configuration
@ConfigurationProperties("app.cors")
class Cors {

	private val logger = LoggerFactory.getLogger(javaClass)!!

	var urls: List<String>? = null

	@Bean
	fun corsFilter(): CorsFilter {
		val configuration = CorsConfiguration()
		logger.info("cors config loaded: " + urls?.joinToString(", "))
		configuration.allowedOrigins =
				urls ?: throw RuntimeException("missing cors url property in environment")
		configuration.allowedMethods = listOf("*")
		configuration.allowedHeaders = listOf("Authorization", "Content-Type")
		val source = UrlBasedCorsConfigurationSource()
		source.registerCorsConfiguration("/**", configuration)

		return CorsFilter(source)
	}

}
