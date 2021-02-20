package ch.judos.snakes.region.core.config.auth

import ch.judos.snakes.region.core.dto.AuthErrorDto
import ch.judos.snakes.region.core.dto.EAuthError
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class JwtRequestFilter @Autowired constructor(
		private val jwtTokenUtil: JwtTokenUtil,
) : OncePerRequestFilter() {

	private val logger = LoggerFactory.getLogger(javaClass)!!

	override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse,
			filterChain: FilterChain) {
		val requestTokenHeader = request.getHeader("Authorization")

		// JWT Token is in the form "Bearer token". Remove Bearer word and get
		// only the Token
		if (requestTokenHeader != null) {
			if (requestTokenHeader.startsWith("Bearer ")) {
				val jwtToken = requestTokenHeader.substring(7)
				try {
					val tokenClaims = jwtTokenUtil.getAllClaimsFromToken(jwtToken)
					if (SecurityContextHolder.getContext().authentication == null) {
						val auth = this.jwtTokenUtil.readToken(tokenClaims)
						SecurityContextHolder.getContext().authentication = auth
					}
				} catch (e: ExpiredJwtException) {
					return AuthErrorDto.jwtError(response, 401, EAuthError.EXPIRED_JWT, "JWT has expired")
				} catch (e: OutdatedJwtException) {
					return AuthErrorDto.jwtError(response, 401, EAuthError.EXPIRED_JWT,
							"JWT has old structure: " + e.message)
				} catch (e: MalformedJwtException) {
					return AuthErrorDto.jwtError(response, 401, EAuthError.INVALID_JWT,
							"JWT has invalid form")
				} catch (e: IllegalArgumentException) {
					return AuthErrorDto.jwtError(response, 401, EAuthError.INVALID_JWT,
							"Could not extract JWT data: " + e.message)
				} catch (e: SecurityException) {
					return AuthErrorDto.jwtError(response, 401, EAuthError.INVALID_JWT,
							"Invalid JWT signature")
				} catch (e: UsernameNotFoundException) {
					return AuthErrorDto.jwtError(response, 401, EAuthError.INVALID_JWT, e.message!!)
				}
			} else {
				return AuthErrorDto.jwtError(response, 401, EAuthError.INVALID_JWT,
						"JWT does not begin with Bearer")
			}
		}
		filterChain.doFilter(request, response)
	}

}
