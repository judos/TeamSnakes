package ch.judos.snakes.region.core.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class AddLogggerInterceptorConfiguration @Autowired constructor(
		private val requestInterceptor: LoggerRequestInterceptor
) : WebMvcConfigurer {

	private val logger = LoggerFactory.getLogger(javaClass)

	override fun addInterceptors(registry: InterceptorRegistry) {
		registry.addInterceptor(requestInterceptor).order(Ordered.HIGHEST_PRECEDENCE)
	}
}
