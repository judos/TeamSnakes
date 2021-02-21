package ch.judos.snakes.region.core.entity

import javax.persistence.*

@Entity
@Table(indexes = [Index(name = "configKey", columnList = "configKey")])
open class Configuration : BaseEntity() {

	@Column(nullable = false, unique = true)
	open var configKey: String? = null

	@Column
	open var description: String? = null

	@Column(nullable = false)
	open var value: String = ""

}
