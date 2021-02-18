package db.migration.v1_0

import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.SingleConnectionDataSource

open class V1_0_0_0__BaseMigration : BaseJavaMigration() {

	private val logger = LoggerFactory.getLogger(javaClass)!!

	override fun migrate(context: Context?) {
		val jdbc = JdbcTemplate(SingleConnectionDataSource(context!!.connection, true))
//		jdbc.execute("")
//
//		val userIds = jdbc.queryForList("select id from test;", Long::class.java)
//		for (id in userIds) {
//			jdbc.execute("update app_user set uuid = '" + UUID.randomUUID() + "' where id = " + id)
//		}

	}

}
