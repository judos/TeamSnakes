package ch.judos.snakes.region

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
@EnableScheduling
class RegionServerLauncher : CommandLineRunner {

	private val logger = LoggerFactory.getLogger(javaClass)!!

	override fun run(vararg args: String?) {
		// logger.info("Java version: " + Runtime.version())
	}

}

fun main(args: Array<String>) {
	runApplication<RegionServerLauncher>(*args)
}
