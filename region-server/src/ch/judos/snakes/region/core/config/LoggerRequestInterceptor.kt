package ch.judos.snakes.region.core.config

import ch.judos.snakes.region.core.entity.AdminUser
import ch.judos.snakes.region.core.service.AuthService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LoggerRequestInterceptor(
		private val authService: AuthService
) : HandlerInterceptorAdapter() {

	private val logger = LoggerFactory.getLogger(javaClass)

	override fun preHandle(request: HttpServletRequest, response: HttpServletResponse,
			handler: Any): Boolean {
		if (request.queryString != null) {
			logger.info("{} {} ? {} ({})", request.method, request.requestURI, request.queryString,
					authInformation())
		} else {
			logger.info("{} {} ({})", request.method, request.requestURI, authInformation())
		}
		return true
	}

	@Throws(Exception::class)
	override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any,
			modelAndView: ModelAndView?) {
		logger.info("{} for {} {} ({})", response.status, request.method, request.requestURI,
				authInformation())
	}

	private fun authInformation(): String {
		return this.authService.getGuestUser()?.let {
			return "$it [GUEST]"
		} ?: this.authService.getAdminUser()?.let<AdminUser, String> {
			return it.username + " (" + it.id + ") " + it.authorities
		} ?: "nologin"
	}
}
