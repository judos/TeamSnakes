package ch.judos.snakes.region.core.service

import ch.judos.snakes.common.service.RandomService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CommonBeans {

	@Bean
	fun randomService(): RandomService {
		return RandomService()
	}
}