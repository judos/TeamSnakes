package ch.judos.snakes.region.extension

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.ComparableExpressionBase

/**
 * Used for easier selection of the order expression in Controllers
 */
fun <T: Comparable<T>> ComparableExpressionBase<T>.order(desc: Boolean): OrderSpecifier<T> {
	return if (desc) this.desc() else this.asc()
}
