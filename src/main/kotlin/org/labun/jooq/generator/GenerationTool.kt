package org.labun.jooq.generator

import org.jooq.util.Database
import org.jooq.util.jaxb.Catalog
import org.jooq.util.jaxb.Schema
import org.labun.jooq.generator.config.DatabaseConfig
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

/**
 * @author Konstantin Labun
 */
object GenerationTool {

  fun generate(generator: Generator) {
    val db = generator.configuration.database
    Class.forName(db.driverClass)

    DriverManager.getConnection(db.jdbcUrl, db.username, db.password)
        .use { connection ->
          assertConnectionSuccess(connection)
          generator.generate(createDbMeta(db, connection))
        }
  }

  private fun createDbMeta(db: DatabaseConfig, connection: Connection): Database {
    val dbMeta = Class.forName(db.dbMetaClass).newInstance() as Database
    configureDefaults(dbMeta)
    dbMeta.connection = connection
    return dbMeta
  }

  private fun assertConnectionSuccess(connection: Connection) {
    connection.createStatement().execute("SELECT 1")
  }

  private fun configureDefaults(db: Database) {
    db.setConfiguredSchemata(Arrays.asList(Schema().withInputSchema("")))
    db.setConfiguredCatalogs(Arrays.asList(Catalog().withInputCatalog("")))
    db.includes = arrayOf(".*")
    db.configuredCustomTypes = listOf()
    db.configuredEnumTypes = listOf()
    db.configuredForcedTypes = listOf()
  }
}
