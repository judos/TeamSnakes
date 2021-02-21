package ch.judos.snakes.region.core.services.login

import ch.judos.snakes.region.core.model.enums.ELoginException
import ch.judos.snakes.region.core.model.enums.ELoginType
import ch.judos.snakes.region.core.services.LoginAttemptService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent
import org.springframework.stereotype.Component

@Component
class AuthFailureListener @Autowired constructor(
		private val loginAttemptService: LoginAttemptService
) : ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

	private val logger = LoggerFactory.getLogger(javaClass)!!

	override fun onApplicationEvent(event: AuthenticationFailureBadCredentialsEvent) {
		val auth = event.authentication
		this.logger.warn(
				"Failed authentication: ${auth::class.simpleName}, principal: ${auth.principal}, Details: ${auth.details}")
		this.loginAttemptService.storeAttempt(ELoginType.ADMIN, ELoginException.WRONG_CREDENTIALS,
				auth.principal.toString())
	}

}
