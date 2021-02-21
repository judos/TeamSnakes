package ch.judos.snakes.region.core.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	open var id: Long = 0

	@Column(columnDefinition = "DATETIME(2)")
	@CreatedDate
	open lateinit var createdDate: LocalDateTime

	@Column(columnDefinition = "DATETIME(2)")
	@LastModifiedDate
	open lateinit var updatedDate: LocalDateTime

}
