package org.labun.jooq.generator.task.domain

import org.jooq.util.UniqueKeyDefinition

/**
 * @author Konstantin Labun
 */
data class UniqKey(
    override val definition: UniqueKeyDefinition,
    val name: String,
    val columns: List<Column>,
    val isPrimaryKey: Boolean,
    var table: Table? = null // cross link could be set only after init
) : Model<UniqueKeyDefinition>

