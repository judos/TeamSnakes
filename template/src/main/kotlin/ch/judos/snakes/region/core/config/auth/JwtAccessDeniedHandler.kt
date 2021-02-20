package ch.judos.snakes.region.core.config.auth

import ch.judos.snakes.region.core.dto.ErrorDto
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtAccessDeniedHandler : AccessDeniedHandler {

	override fun handle(request: HttpServletRequest, response: HttpServletResponse,
		accessDeniedException: AccessDeniedException) {
		ErrorDto.missingRole.writeToResponse(response)
	}

}
