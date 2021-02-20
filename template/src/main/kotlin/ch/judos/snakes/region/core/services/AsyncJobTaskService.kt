package ch.judos.snakes.region.core.services

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service


@Service
class AsyncJobTaskService {
	@Async
	fun execute(runnable: Runnable) {
		runnable.run()
	}
}
