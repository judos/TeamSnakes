package ch.judos.snakes.region.core.services

import ch.judos.snakes.region.core.repository.ConfigurationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class ConfigurationService @Autowired constructor(
		private val configRepo: ConfigurationRepository
) {

//	fun getConfiguration(): ConfigurationDto {
//		val config = fetch(EConfigurationKey.TEST).value
//		return ConfigurationDto(config)
//	}

//	fun fetch(key: EConfigurationKey): Configuration {
//		return this.configRepo.findOne(QConfiguration.configuration.configKey.eq(key.configKey)).orElseThrow()
//	}

}
