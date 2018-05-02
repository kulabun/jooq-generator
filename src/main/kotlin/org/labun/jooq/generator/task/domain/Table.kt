package org.labun.jooq.generator.task.domain

import org.jooq.util.TableDefinition

/**
 * @author Konstantin Labun
 */
data class Table(
    override val definition: TableDefinition,
    val name: String,
    val columns: List<Column>,
    val identity: Identity?,
    val uniqKeys: List<UniqKey>,

    val simpleTableClassName: String,
    val canonicalTableClassName: String,

    val simpleColumnsClassName: String,
    val canonicalColumnsClassName: String,

    val simpleRecordClassName: String,
    val canonicalRecordClassName: String,

    var schema: Schema? = null, // cross link could be set only after init
    var primaryKey: UniqKey? = null,
    var indexes: List<Index> = listOf(),
    var foreignKeys: List<ForeignKey> = listOf()
) : Model<TableDefinition>