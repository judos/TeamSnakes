package ch.judos.snakes.region.extension

import com.querydsl.core.types.Predicate
import org.springframework.data.querydsl.QuerydslPredicateExecutor


/**
 * instead of the optional of the findOne method this method return a nullable object of type T?
 * @param predicate must not be <i>null</i>.
 * @throws IncorrectResultSizeDataAccessException  - if the predicate yields more than one result.
 */
fun <T> QuerydslPredicateExecutor<T>.findOneOrNull(predicate: Predicate): T? {
	return this.findOne(predicate).orElse(null)
}

