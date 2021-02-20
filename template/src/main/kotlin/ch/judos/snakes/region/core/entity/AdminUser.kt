package ch.judos.snakes.region.core.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Size
import kotlin.collections.HashSet


@Entity
@Table()
open class AdminUser : BaseEntity(), UserDetails {

	@Column(unique = true, nullable = false)
	@Size(max = 20)
	private lateinit var username: String

	@Column(nullable = false)
	@Size(max = 120)
	internal open lateinit var password: String

	@Column
	open var lastActive: LocalDateTime? = null

	@Column(unique = true, nullable = false, length = 36)
	open var uuid: String = UUID.randomUUID()!!.toString()

	@OneToMany(mappedBy = "adminUser", fetch = FetchType.EAGER)
	open var roleAdmins: MutableSet<AdminUserRole> = HashSet()


	override fun getUsername(): String {
		return this.username
	}

	override fun getPassword(): String {
		return this.password
	}

	override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
		val result = mutableListOf<GrantedAuthority>()
		result.addAll(this.roleAdmins)
		return result
	}

	override fun isAccountNonExpired(): Boolean {
		return true
	}

	override fun isAccountNonLocked(): Boolean {
		return true
	}

	override fun isCredentialsNonExpired(): Boolean {
		return true
	}

	override fun isEnabled(): Boolean {
		return true
	}
}

