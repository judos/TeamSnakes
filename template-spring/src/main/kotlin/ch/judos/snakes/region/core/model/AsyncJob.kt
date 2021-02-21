@file:Suppress("unused")

package ch.judos.snakes.region.core.model

import ch.judos.snakes.region.core.dto.ErrorDto
import ch.judos.snakes.region.extension.toHHMMSS
import com.fasterxml.jackson.annotation.JsonIgnore
import org.apache.commons.lang3.tuple.MutablePair
import org.slf4j.LoggerFactory
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@Suppress("unused")
open class AsyncJob(var jobId: String, logTimeRegularly: Boolean, val autoCleanupMin: Int) {

	companion object {
		const val MAX_ERROR = 1000
	}

	private val logger = LoggerFactory.getLogger(javaClass)

	private val logTimeRegularly: Boolean
	var started: LocalDateTime
		protected set

	var finished: LocalDateTime? = null
		protected set

	/**
	 * measured progress in percent `[0, 100]` uses configured substeps to calculate the correct total progress
	 */
	var progress = 0.0
		@Synchronized protected set(progress) {
			var progress = progress
			if (progressSubSteps.size > 0) {
				for (i in progressSubSteps.indices.reversed()) {
					val subCurrentStep: Int = progressSubSteps[i].left
					val subTotalSteps: Int = progressSubSteps[i].right
					progress = ((subCurrentStep - 1.0) * 100.0 + progress) / subTotalSteps
				}
			}
			field = progress
			if (logTimeRegularly) {
				checkPrintProgressLog()
			}
		}

	var status: String
	var result: Any? = null
		protected set

	protected var errors: MutableList<ErrorDto>
	protected var warnings: MutableList<ErrorDto>
	protected var infos: MutableList<ErrorDto>
	protected var progressSubSteps: MutableList<MutablePair<Int, Int>>

	@Transient
	protected var lastLog: Long

	@get:JsonIgnore
	val durationString: String
		get() {
			val ms = ChronoUnit.MILLIS.between(started, LocalDateTime.now())
			return ms.toHHMMSS()
		}

	init {
		started = LocalDateTime.now()
		errors = ArrayList<ErrorDto>()
		warnings = ArrayList<ErrorDto>()
		infos = ArrayList<ErrorDto>()
		progressSubSteps = ArrayList()
		this.logTimeRegularly = logTimeRegularly
		status = "Bereitet vor..."
		lastLog = System.currentTimeMillis()
	}

	/**
	 * subsequent calls to setProgress will automatically adapt the actual progress.
	 * e.g. 2 Methods are doing work. Before we pass the Progress method to the first we call beginSubstepsWithTotalSteps(2, "x")
	 * method 1 of 2 calls setProgress(50), this will set an actual progress of 25%.<br></br>
	 * After the work is done in the first method we call popProgressPart() to remove the mapping.
	 *
	 * @param totalSteps [2-x]
	 */
	@Synchronized
	fun beginSubstepsWithTotalSteps(totalSteps: Int, status: String?) {
		progressSubSteps.add(MutablePair(1, totalSteps))
		if (status != null) {
			setProgressState(0.0, status)
		} else {
			this.progress = 0.0
		}
	}

	@Synchronized
	fun beginSubstepsWithTotalSteps(totalSteps: Int) {
		this.beginSubstepsWithTotalSteps(totalSteps, null)
	}

	@Synchronized
	fun beginNextSubstep(status: String? = null) {
		if (progressSubSteps.size == 0) {
			throw RuntimeException("Cannot proceed with next step when no substep sequence has begon")
		}
		val currentSubsteps = progressSubSteps.last()
		// increase currentStep
		currentSubsteps.left++
		// check, it's fine if it goes up 1 higher than it should (easier to use in for-loops)
		if (currentSubsteps.left > currentSubsteps.right + 1) {
			throw RuntimeException(
					"Current step can not be larger than total step count of: " + currentSubsteps.right)
		}
		this.progress = 0.0
		if (status != null) {
			this.status = status
		}
	}

	@Synchronized
	fun finishSubsteps() {
		if (progressSubSteps.size > 0) {
			this.progress = 100.0
			progressSubSteps.removeAt(progressSubSteps.size - 1)
		}
	}

	fun isOlderThanMinutes(minutes: Int): Boolean {
		return if (finished == null) {
			false
		} else {
			Duration.between(finished, LocalDateTime.now()).toMinutes() >= minutes
		}
	}

	fun setResult(result: Any?, status: String?) {
		finished = LocalDateTime.now()
		progress = 100.0
		this.status = status ?: "Fertig"
		this.result = result
	}

	fun setFailed(e: BusinessException) {
		finished = LocalDateTime.now()
		status = e.message
	}

	fun setFailed(e: Throwable) {
		finished = LocalDateTime.now()
		var exceptionMessage = "Ein technisches Problem ist aufgetreten: " + e.message
		if (e.message == null || e.message!!.isEmpty()) {
			exceptionMessage =
					"Ein technisches Problem ist aufgetreten: $e"
		}
		status = exceptionMessage
		addError(ErrorDto("ERROR", exceptionMessage, e))
	}

	fun isFinished(): Boolean {
		return finished != null
	}

	fun addError(error: ErrorDto) {
		if (errors.size == MAX_ERROR - 1) {
			errors.add(ErrorDto("TOO_MANY",
					"Es gibt mehr als $MAX_ERROR Meldungen. Die weiteren Einträge werden nicht mehr angezeigt."))
		} else if (errors.size < MAX_ERROR - 1) {
			errors.add(error)
		}
	}

	fun addWarning(error: ErrorDto) {
		if (warnings.size == MAX_ERROR - 1) {
			warnings.add(ErrorDto("TOO_MANY",
					"Es gibt mehr als $MAX_ERROR Meldungen. Die weiteren Einträge werden nicht mehr angezeigt."))
		} else if (warnings.size < MAX_ERROR - 1) {
			warnings.add(error)
		}
	}

	fun addInformation(error: ErrorDto) {
		if (infos.size == MAX_ERROR - 1) {
			infos.add(ErrorDto("TOO_MANY",
					"Es gibt mehr als $MAX_ERROR Meldungen. Die weiteren Einträge werden nicht mehr angezeigt."))
		} else if (infos.size < MAX_ERROR - 1) {
			infos.add(error)
		}
	}

	@Synchronized
	fun checkPrintProgressLog() {
		if (System.currentTimeMillis() - lastLog > 30 * 1000) {
			lastLog = System.currentTimeMillis()
			logger.info(this.toString())
		}
	}

	override fun toString(): String {
		val formatter: NumberFormat = DecimalFormat("#0.#")
		return "job " + jobId + ", time since start " + this.durationString + ", progress: " +
				formatter.format(progress) + "%, state: " + status
	}

	fun setProgressState(progress: Double, status: String) {
		this.progress = progress
		this.status = status
	}

}
