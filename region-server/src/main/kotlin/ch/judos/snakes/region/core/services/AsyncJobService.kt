package ch.judos.snakes.region.core.services

import ch.judos.snakes.region.core.model.AsyncJob
import ch.judos.snakes.region.core.model.BusinessException
import ch.judos.snakes.region.core.model.RunnableWithProgressAndException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.HashMap

@Service
class AsyncJobService @Autowired constructor(
		private val asyncJobTaskService: AsyncJobTaskService
) {

	private val logger = LoggerFactory.getLogger(javaClass)

	private var nextJobId = 0L
	private val jobs: HashMap<String, AsyncJob>

	init {
		nextJobId = Random().nextInt(1000).toLong() // use random starting id incase server restarts
		jobs = HashMap()
	}

	/**
	 * @param type if left blank an number will be chosen automatically
	 * @param autoCleanupMin the result of the job will be automatically cleaned from the job list after given minutes.
	 * 	Set to 0 to keep the data forever
	 * 	@return jobId given by type parameter or automatically assigned if parameter was left null
	 */
	@Synchronized
	fun createAsyncJob(type: String?, autoCleanupMin: Int = 5, task: RunnableWithProgressAndException): AsyncJob {
		val id = type ?: (this.nextJobId++).toString()
		val job = AsyncJob(id, true, autoCleanupMin)
		logger.info("Job $id added to queue")
		this.asyncJobTaskService.execute {
			try {
				logger.info("Job " + job.jobId + " gestartet")
				task.run(job)
			} catch (e: BusinessException) {
				job.setFailed(e)
			} catch (e: Exception) {
				e.printStackTrace()
				job.setFailed(e)
			} finally {
				logger.info("Job finished: $job")
			}
		}
		jobs[job.jobId] = job
		return job
	}

	fun getJob(jobId: String): AsyncJob? {
		return jobs[jobId]
	}

	@Scheduled(fixedDelay = 1000 * 30) // run every 30s
	fun cleanupOldJobData() {
		val it: MutableIterator<AsyncJob> = jobs.values.iterator()
		while (it.hasNext()) {
			val x: AsyncJob = it.next()
			if (!x.isFinished()) {
				x.checkPrintProgressLog()
			}
			if (x.isFinished() && x.autoCleanupMin > 0 && x.isOlderThanMinutes(x.autoCleanupMin)) {
				it.remove()
				logger.info("Cleanup data of: $x")
			}
		}
	}
}
