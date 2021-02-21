package ch.judos.snakes.region.core.controller

import ch.judos.snakes.region.core.model.AsyncJob
import ch.judos.snakes.region.core.services.AsyncJobService
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("admin/jobs", produces = ["application/json"])
@SecurityRequirement(name = "jwt_auth")
class JobController @Autowired constructor(
		private val asyncJobService: AsyncJobService
) {

	@GetMapping("{jobId}")
	fun getState(@PathVariable jobId: String): ResponseEntity<*>? {
		val progress: AsyncJob =
				this.asyncJobService.getJob(jobId) ?: return ResponseEntity.notFound().build<Any>()
		return ResponseEntity.ok<Any>(progress)
	}
}
