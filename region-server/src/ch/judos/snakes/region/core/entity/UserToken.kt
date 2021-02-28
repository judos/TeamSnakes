package ch.judos.snakes.region.core.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.Table

@Entity
@Table(indexes = [Index(name = "username", columnList = "username")])
open class UserToken : BaseEntity() {

	@Column(nullable = false)
	open lateinit var username: String

	@Column(nullable = false, length = 64)
	open lateinit var token: String

}