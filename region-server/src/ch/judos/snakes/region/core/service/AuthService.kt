package ch.judos.snakes.region.core.service

import ch.judos.snakes.region.core.entity.AdminUser
import ch.judos.snakes.region.core.repository.AdminUserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AuthService @Autowired constructor(
		private val adminUserRepository: AdminUserRepository,
) {

	private val logger = LoggerFactory.getLogger(javaClass)!!

	fun getAdminUser(): AdminUser? {
		val auth = SecurityContextHolder.getContext().authentication
		if (auth is UsernamePasswordAuthenticationToken) {
			val login = auth.principal
			if (login is Long) {
				// fetch object with id
				// if object would be stored in principal it would be in detached state (request filter did already end session)
				return this.adminUserRepository.findByIdOrNull(login)?.let {
					it.lastActive = LocalDateTime.now()
					this.adminUserRepository.save(it)
				}
			}
		}
		return null
	}

	fun getGuestUser(): String? {
		val auth = SecurityContextHolder.getContext().authentication
		if (auth is AnonymousAuthenticationToken) {
			val user = auth.principal
			if (user is String) {
				return user
			}
		}
		return null
	}

}
