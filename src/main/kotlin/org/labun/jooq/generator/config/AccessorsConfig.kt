package org.labun.jooq.generator.config

/**
 * @author Konstantin Labun
 */
data class AccessorsConfig(
    var getters: NameConfig = NameConfig(),
    var setters: NameConfig = NameConfig(),
    var fluent: Boolean = true
)