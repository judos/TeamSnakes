package ch.judos.snakes.region.core.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration


@Configuration
@ConfigurationProperties(prefix = "app.settings")
class AppProperties {

	var regionName: String = ""
	var otherRegions: List<String> = listOf()

}
