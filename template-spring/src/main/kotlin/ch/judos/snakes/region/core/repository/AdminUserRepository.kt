package ch.judos.snakes.region.core.repository

import ch.judos.snakes.region.core.entity.AdminUser
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AdminUserRepository : CrudRepository<AdminUser, Long>,
		QuerydslPredicateExecutor<AdminUser> {

	fun getByUsername(username: String): AdminUser?

}
