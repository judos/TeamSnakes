package ch.judos.snakes.region.core.controller

import ch.judos.snakes.region.core.dto.InfoDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("", produces = ["application/json"])
class InfoController @Autowired constructor(
) {
	@Value("\${application.name}")
	private val applicationName: String? = null

	@Value("\${build.version}")
	private val buildVersion: String? = null

	@Value("\${build.timestamp}")
	private val buildTimestamp: String? = null


	private val logger = LoggerFactory.getLogger(javaClass)!!

	@GetMapping("/version")
	fun getInfo(): ResponseEntity<Any> {
		return ResponseEntity.ok(InfoDto(buildVersion, applicationName, buildTimestamp))
	}

}
