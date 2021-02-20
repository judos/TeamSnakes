package ch.judos.snakes.region.core.entity

import ch.judos.snakes.region.core.model.enums.EUserRole
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction
import org.springframework.security.core.GrantedAuthority
import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["admin_user_id", "role"])])
open class AdminUserRole : BaseEntity(), GrantedAuthority {

	@ManyToOne(optional = false, cascade = [CascadeType.ALL])
	@NotFound(action = NotFoundAction.EXCEPTION)
	@JoinColumn(name = "admin_user_id")
	open lateinit var adminUser: AdminUser

	@Column(nullable = false, columnDefinition = "enum('USER','ADMIN','GAME_SERVER')")
	@Enumerated(EnumType.STRING)
	open lateinit var role: EUserRole

	override fun getAuthority(): String {
		return this.role.name
	}

}
