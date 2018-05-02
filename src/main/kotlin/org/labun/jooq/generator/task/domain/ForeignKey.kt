package org.labun.jooq.generator.task.domain

import org.jooq.util.ForeignKeyDefinition

/**
 * @author Konstantin Labun
 */
data class ForeignKey(
    override val definition: ForeignKeyDefinition,
    val name: String,
    val columns: List<Column>,
    val refKey: UniqKey,
    var table: Table? = null // cross link could be set only after init
) : Model<ForeignKeyDefinition>

