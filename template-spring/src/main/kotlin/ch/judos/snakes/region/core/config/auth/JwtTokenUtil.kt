package ch.judos.snakes.region.core.config.auth

import ch.judos.snakes.region.core.entity.AdminUser
import ch.judos.snakes.region.core.entity.QAdminUser
import ch.judos.snakes.region.core.model.enums.EUserRole
import ch.judos.snakes.region.core.repository.AdminUserRepository
import ch.judos.snakes.region.extension.containsAllKeys
import ch.judos.snakes.region.extension.findOneOrNull
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap

@Component
class JwtTokenUtil @Autowired constructor(
		@Value("\${app.jwt.secret}") private val secret: String,
		private val adminUserRepo: AdminUserRepository,
) : Serializable {

	@Suppress("unused")
	private val logger = LoggerFactory.getLogger(javaClass)!!

	//for retrieveing any information from token we will need the secret key
	fun getAllClaimsFromToken(token: String?): Claims {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body
	}

	//generate token for user
	fun generateToken(userDetails: AdminUser): String {
		val claims = HashMap<String, Any>()
		claims[JWT_FIELD_USER_TYPE] = EUserRole.ADMIN.name
		claims[JWT_FIELD_USER_UUID] = userDetails.uuid
		claims[JWT_FIELD_VERSION] = JWT_VERSION
		return doGenerateToken(claims, userDetails.username)
	}

	fun readToken(tokenClaims: Claims): Authentication? {
		if (!tokenClaims.containsAllKeys(JWT_FIELD_VERSION, JWT_FIELD_USER_UUID, JWT_FIELD_USER_TYPE)) {
			throw IllegalArgumentException("Missing jwt claims")
		}
		val uuid = tokenClaims[JWT_FIELD_USER_UUID] as String
		val version = tokenClaims[JWT_FIELD_VERSION] as Int
		val type = tokenClaims[JWT_FIELD_USER_TYPE] as String
		if (version < JWT_VERSION) {
			throw OutdatedJwtException(
					"JWT is of version $version, but backend only accepts version $JWT_VERSION")
		}
		if (type == EUserRole.ADMIN.name) {
			return readAdminToken(tokenClaims, uuid)
		}
		throw IllegalArgumentException("Unknown jwt token type: $type")
	}

	private fun readAdminToken(tokenClaims: Claims, uuid: String): Authentication {
		val username = tokenClaims.subject
		val user = this.adminUserRepo.findOneOrNull(
				QAdminUser.adminUser.username.eq(username).and(QAdminUser.adminUser.uuid.eq(uuid)))
				?: throw UsernameNotFoundException("User not found with username: $tokenClaims.subject")
		val auth = UsernamePasswordAuthenticationToken(user.id, null, user.authorities)
		auth.details = EUserRole.ADMIN.name
		return auth
	}

	//while creating the token -
	//1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
	//2. Sign the JWT using the HS512 algorithm and secret key.
	//3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
	//   compaction of the JWT to a URL-safe string
	private fun doGenerateToken(claims: Map<String, Any>, subject: String): String {
		return Jwts.builder().setClaims(claims).setSubject(subject)
				.setIssuedAt(Date(System.currentTimeMillis()))
				.signWith(SignatureAlgorithm.HS512, secret).compact()
	}


	companion object {
		private const val serialVersionUID = -2550185165626007488L

		const val JWT_FIELD_USER_TYPE = "type"

		const val JWT_FIELD_USER_UUID = "uuid"

		const val JWT_FIELD_VERSION = "ver"

		const val JWT_VERSION = 1
	}
}
