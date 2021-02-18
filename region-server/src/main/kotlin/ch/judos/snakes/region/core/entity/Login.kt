package ch.judos.snakes.region.core.entity

import ch.judos.snakes.region.core.model.enums.ELoginException
import ch.judos.snakes.region.core.model.enums.ELoginType
import java.time.LocalDateTime
import javax.persistence.*


@Entity
open class Login {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	open var id: Long = 0

	@Column(length = 50)
	open lateinit var ip: String

	@Column
	open var success: Boolean = false

	@Column(columnDefinition = "enum('WRONG_CREDENTIALS', 'BLOCKED', 'OTP_EXPIRED')")
	@Enumerated(EnumType.STRING)
	open var exception: ELoginException? = null

	@Column(nullable = false, columnDefinition = "enum('ADMIN')")
	@Enumerated(EnumType.STRING)
	open lateinit var loginType: ELoginType

	@Column
	open lateinit var dateTime: LocalDateTime

	@Column
	open lateinit var principal: String
}
