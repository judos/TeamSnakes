package ch.judos.snakes.region.core.service

import ch.judos.snakes.region.core.entity.QUserToken
import ch.judos.snakes.region.core.repository.UserTokenRepository
import ch.judos.snakes.region.extension.findOneOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class UserTokenService @Autowired constructor(
		private val userTokenRepository: UserTokenRepository
) {

	fun isValid(username: String, token: String): Boolean {
		val token = this.userTokenRepository.findOneOrNull(
				QUserToken.userToken.username.eq(username)
						.and(QUserToken.userToken.token.eq(token)))
		if (token != null) {
			this.userTokenRepository.delete(token)
		}
		return token != null
	}
}