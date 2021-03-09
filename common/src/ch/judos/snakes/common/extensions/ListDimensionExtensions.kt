package ch.judos.snakes.common.extensions

import java.awt.Dimension
import kotlin.math.max


fun List<Dimension>.maxSum(): Dimension {
	var width = 0
	var height = 0
	for (c in this) {
		width = max(width, c.width)
		height += c.height
	}
	return Dimension(width, height)
}

fun List<Dimension>.sumMax(): Dimension {
	var width = 0
	var height = 0
	for (c in this) {
		width += c.width
		height = max(height, c.height)
	}
	return Dimension(width, height)
}