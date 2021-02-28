package ch.judos.snakes.region.core.config


import ch.judos.snakes.region.core.entity.AdminUser
import ch.judos.snakes.region.core.repository.AdminUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsAdminService @Autowired constructor(
		private val adminUserRepo: AdminUserRepository
) : UserDetailsService {

	@Throws(UsernameNotFoundException::class)
	override fun loadUserByUsername(username: String): AdminUser {
		return this.adminUserRepo.getByUsername(username) ?: throw UsernameNotFoundException(
				"User not found with username: $username")
	}

}
