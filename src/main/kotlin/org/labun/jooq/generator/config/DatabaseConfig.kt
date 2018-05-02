package org.labun.jooq.generator.config

/**
 * @author Konstantin Labun
 */
data class DatabaseConfig(
    var username: String = "",
    var password: String = "",
    var driverClass: String = "",
    var dbMetaClass: String = "",
    var jdbcUrl: String = "",
    var schemas: List<String> = emptyList()
)