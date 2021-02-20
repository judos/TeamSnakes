package ch.judos.snakes.region.core.services

import ch.judos.snakes.region.core.entity.Login
import ch.judos.snakes.region.core.entity.QLogin
import ch.judos.snakes.region.core.model.enums.ELoginException
import ch.judos.snakes.region.core.model.enums.ELoginType
import ch.judos.snakes.region.core.repository.LoginAttemptRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.LocalDateTime


@Service
class LoginAttemptService @Autowired constructor(
		private val loginAttemptRepo: LoginAttemptRepository
) {

	private val logger = LoggerFactory.getLogger(javaClass)!!

	fun storeAttempt(type: ELoginType, exception: ELoginException?, principal: String) {
		val attempt = Login()
		attempt.ip = getClientIp()
		attempt.success = exception == null
		attempt.exception = exception
		attempt.loginType = type
		attempt.dateTime = LocalDateTime.now()
		attempt.principal = principal
		this.loginAttemptRepo.save(attempt)
	}

	fun isBlocked(type: ELoginType, principal: String): Boolean {
		val failedAttempts = getFailedAttempts(type)
		if (failedAttempts >= 4) {
			Thread.sleep((failedAttempts - 3).coerceAtMost(5) * 1000)
		}
		if (failedAttempts >= 10) {
			storeAttempt(type, ELoginException.BLOCKED, principal)
			return true
		}
		return false
	}

	fun getFailedAttempts(type: ELoginType): Long {
		val checkBackTo = LocalDateTime.now().minusMinutes(15)
		val filter = QLogin.login.dateTime.after(checkBackTo)
				.and(QLogin.login.success.isFalse)
				.and(QLogin.login.ip.eq(getClientIp()))
				.and(QLogin.login.loginType.eq(type))
				.and(QLogin.login.exception.eq(ELoginException.WRONG_CREDENTIALS))
		return this.loginAttemptRepo.count(filter)
	}


	fun getClientIp(): String {
		val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
		for (header in IP_HEADER_CANDIDATES) {
			val ipList = request.getHeader(header)
			if (ipList != null && ipList.isNotEmpty() && !"unknown".equals(ipList, ignoreCase = true)) {
				return ipList.split(",".toRegex()).toTypedArray()[0]
			}
		}
		return request.remoteAddr
	}


	companion object {
		private val IP_HEADER_CANDIDATES = arrayOf(
				"X-Forwarded-For",
				"Proxy-Client-IP",
				"WL-Proxy-Client-IP",
				"HTTP_X_FORWARDED_FOR",
				"HTTP_X_FORWARDED",
				"HTTP_X_CLUSTER_CLIENT_IP",
				"HTTP_CLIENT_IP",
				"HTTP_FORWARDED_FOR",
				"HTTP_FORWARDED",
				"HTTP_VIA",
				"REMOTE_ADDR"
		)
	}

}
