package org.labun.jooq.generator.task.domain

import org.jooq.util.ColumnDefinition

/**
 * @author Konstantin Labun
 */
data class Column(
    override val definition: ColumnDefinition,
    val name: String,
    val sqlType: String,
    val javaName: String,
    val javaType: String,
    val setterName: String,
    val getterName: String,

    var table: Table? = null // cross link could be set only after init
) : Model<ColumnDefinition>