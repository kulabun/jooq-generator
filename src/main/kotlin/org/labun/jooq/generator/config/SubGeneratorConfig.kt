package org.labun.jooq.generator.config

import org.labun.jooq.generator.util.DefaultJavaTypeResolver
import org.labun.jooq.generator.util.DefaultNameCreator
import org.labun.jooq.generator.util.DefaultSqlTypeResolver

/**
 * @author Konstantin Labun
 */
data class SubGeneratorConfig(
    var generatorName: String = "",
    var generatedSourcesRoot: String = "",
    var packageName: String = "",
    var className: NameConfig = NameConfig(),
    var accessors: AccessorsConfig = AccessorsConfig(),
    var nameCreator: String = DefaultNameCreator::class.java.canonicalName,
    var template: String = "",
    var javaTimeDates: Boolean = false,
    var sqlTypeResolver: String = DefaultSqlTypeResolver::class.java.canonicalName,
    var javaTypeResolver: String = DefaultJavaTypeResolver::class.java.canonicalName,
    var subGenerator: String = ""
)