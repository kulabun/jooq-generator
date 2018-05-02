package org.labun.jooq.generator.task.domain

import org.jooq.util.SchemaDefinition

/**
 * @author Konstantin Labun
 */
data class Schema(
    override val definition: SchemaDefinition,
    val name: String,
    val tables: List<Table>,
    val sequences: List<Sequence>,

    val simpleSchemaClassName: String,
    val canonicalSchemaClassName: String,

    val simpleSequencesClassName: String,
    val canonicalSequencesClassName: String,

    val simpleIndexesClassName: String,
    val canonicalIndexesClassName: String,

    val simpleKeysClassName: String,
    val canonicalKeysClassName: String,

    var catalog: Catalog? = null // cross link could be set only after init
) : Model<SchemaDefinition>
