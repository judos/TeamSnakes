package ch.judos.snakes.region.core.services.login

import ch.judos.snakes.region.core.model.enums.ELoginType
import ch.judos.snakes.region.core.services.LoginAttemptService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component


@Component
class AuthSuccessListener @Autowired constructor(
		private val loginAttemptService: LoginAttemptService
) : ApplicationListener<AuthenticationSuccessEvent> {

	private val logger = LoggerFactory.getLogger(javaClass)!!

	override fun onApplicationEvent(event: AuthenticationSuccessEvent) {
		val principal = event.authentication.principal
		var principalStr = principal.toString()
		if (principal is UserDetails) {
			principalStr = principal.username
		}
		this.loginAttemptService.storeAttempt(ELoginType.ADMIN, null, principalStr)
	}


}
