package ch.judos.snakes.region.core.services

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration


@Configuration
@ConfigurationProperties(prefix = "app.import")
class ImportProperties {

	var anonymized: Boolean = false

}
