package org.labun.jooq.generator.task.domain

import org.jooq.util.IndexDefinition

/**
 * @author Konstantin Labun
 */
data class Index(
    override val definition: IndexDefinition,
    val name: String,
    val columns: List<Column>,
    val isUnique: Boolean,

    var table: Table? = null // cross link could be set only after init
) : Model<IndexDefinition>
