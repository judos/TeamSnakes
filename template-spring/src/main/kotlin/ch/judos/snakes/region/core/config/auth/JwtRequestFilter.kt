package ch.judos.snakes.region.core.config.auth

import ch.judos.snakes.region.core.dto.ErrorDto
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
					return ErrorDto.invalidJwt("JWT has expired").writeToResponse(response)
				} catch (e: OutdatedJwtException) {
					return ErrorDto.invalidJwt("JWT has old structure: " + e.message)
						.writeToResponse(response)
				} catch (e: MalformedJwtException) {
					return ErrorDto.invalidJwt("JWT has invalid form").writeToResponse(response)
				} catch (e: IllegalArgumentException) {
					return ErrorDto.invalidJwt("Could not extract JWT data: " + e.message)
						.writeToResponse(response)
				} catch (e: SecurityException) {
					return ErrorDto.invalidJwt("Invalid JWT signature").writeToResponse(response)
				} catch (e: UsernameNotFoundException) {
					return ErrorDto.invalidJwt(e.message!!).writeToResponse(response)
				}
			} else {
				return ErrorDto.invalidJwt("JWT does not begin with Bearer").writeToResponse(response)
			}
		}
		filterChain.doFilter(request, response)
	}

}
