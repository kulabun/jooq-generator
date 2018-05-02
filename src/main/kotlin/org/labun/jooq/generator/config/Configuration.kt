package org.labun.jooq.generator.config

import java.util.*

/**
 * @author Konstantin Labun
 */
data class Configuration(
    var database: DatabaseConfig = DatabaseConfig(),
    var subGenerators: List<SubGeneratorConfig> = emptyList()
)