package org.labun.jooq.generator.task.domain

import org.jooq.util.IdentityDefinition

/**
 * @author Konstantin Labun
 */
data class Identity(
    override val definition: IdentityDefinition,
    val name: String,
    val column: Column,

    var table: Table? = null // cross link could be set only after init
) : Model<IdentityDefinition>
