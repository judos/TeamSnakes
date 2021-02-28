package ch.judos.snakes.region.extension

import kotlin.math.roundToInt

/**
 * Show ms in format `[hh'h'] [mm'm'] [ss's'] [ms'ms']` see examples:
 * ```
 * 1h 02m 03s
 * 1m 02s
 * 10s
 * 9.9s
 * 500ms
 * ```
 */
fun Long.toHHMMSS(): String {
	val hours = this / 1000 / 3600
	val minutes = (this / 1000 / 60) % 60
	val seconds = (this / 1000) % 60
	val ms = this % 1000

	var result = ""
	if (hours > 0) {
		result += hours.toString() + "h"
	}
	if (hours > 0 || minutes > 0) {
		if (hours > 0 && minutes < 10) {
			result += "0"
		}
		result += minutes.toString() + "m"
	}
	if (seconds >= 10) {
		result += seconds.toString() + "s"
	} else if (result.length > 0) {
		result += "0" + seconds + "s"
	} else if (seconds > 0) {
		val s = seconds + ms / 1000.0
		result += ((s * 10).roundToInt() / 10).toString() + "s"
	} else if (seconds == 0L) {
		result += ms.toString() + "ms"
	}
	return result
}
