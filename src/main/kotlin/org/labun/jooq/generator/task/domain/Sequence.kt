package org.labun.jooq.generator.task.domain

import org.jooq.util.SequenceDefinition

/**
 * @author Konstantin Labun
 */
data class Sequence(
    override val definition: SequenceDefinition,
    val name: String,
    val javaType: String,
    val sqlType: String,

    var schema: Schema? = null
) : Model<SequenceDefinition>
