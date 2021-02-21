package ch.judos.snakes.region.core.repository

import ch.judos.snakes.region.core.entity.Configuration
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ConfigurationRepository : CrudRepository<Configuration, Long>,
		QuerydslPredicateExecutor<Configuration>
