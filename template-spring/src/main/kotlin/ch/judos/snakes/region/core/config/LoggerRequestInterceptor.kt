package ch.judos.snakes.region.core.config

import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LoggerRequestInterceptor : HandlerInterceptorAdapter() {
	private val logger = LoggerFactory.getLogger(javaClass)

	override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
		if (request.queryString != null) {
			logger.info("{} {} ? {} ({})", request.method, request.requestURI, request.queryString, authInformation())
		} else {
			logger.info("{} {} ({})", request.method, request.requestURI, authInformation())
		}
		return true
	}

	@Throws(Exception::class)
	override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any,
			modelAndView: ModelAndView?) {
		logger.info("{} for {} {} ({})", response.status, request.method, request.requestURI, authInformation())
	}

	private fun authInformation(): String {
		val auth = SecurityContextHolder.getContext().authentication
		if (auth is UsernamePasswordAuthenticationToken) {
			try {
				return auth.details.toString() + " " + auth.principal.toString()
			} catch (e: IllegalArgumentException) {
				e.printStackTrace()
			}
		}
		return "nologin"
	}
}
