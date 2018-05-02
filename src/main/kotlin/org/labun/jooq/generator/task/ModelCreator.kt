package org.labun.jooq.generator.task

import org.jooq.util.*
import org.labun.jooq.generator.generation.GeneratorsNames
import org.labun.jooq.generator.task.domain.*
import org.labun.jooq.generator.util.mapNotNull

/**
 * @author Konstantin Labun
 */
class ModelCreator(
    private val subGenerators: List<SubGeneratorContext>,
    private val schemaFilter: (schema: SchemaDefinition) -> Boolean
) {

  fun create(schemaDefinition: SchemaDefinition) = createSchema(schemaDefinition)

  fun create(catalogDefinition: CatalogDefinition) = createCatalog(catalogDefinition)

  private fun createCatalog(def: CatalogDefinition) =
      Catalog(
          definition = def,
          name = def.name,
          simpleCatalogClassName = getCatalogGeneratorContext().resolveSimpleClassName(def),
          canonicalCatalogClassName = getCatalogGeneratorContext().resolveCanonicalClassName(def),
          schemas = def.schemata.mapNotNull { createSchemas(it) }
      ).apply {
        schemas.forEach { it.catalog = this }
      }

  private fun createSchemas(schemas: List<SchemaDefinition>) =
      schemas.filter(schemaFilter).map { createSchema(it) }

  private fun createSchema(def: SchemaDefinition) =
      Schema(
          definition = def,
          name = def.name,
          tables = createTables(def.tables),
          sequences = def.database.getSequences(def).mapNotNull { createSequences(it) },
          simpleSequencesClassName = getSchemaSequencesContext().resolveSimpleClassName(def),
          canonicalSequencesClassName = getSchemaSequencesContext().resolveCanonicalClassName(def),
          simpleIndexesClassName = getSchemaIndexesContext().resolveSimpleClassName(def),
          canonicalIndexesClassName = getSchemaIndexesContext().resolveCanonicalClassName(def),
          simpleKeysClassName = getSchemaKeysContext().resolveSimpleClassName(def),
          canonicalKeysClassName = getSchemaKeysContext().resolveCanonicalClassName(def),
          simpleSchemaClassName = getSchemaContext().resolveSimpleClassName(def),
          canonicalSchemaClassName = getSchemaContext().resolveCanonicalClassName(def)
      ).apply {
        tables.forEach { it.schema = this }
        createForeignKeys(this)
        sequences.forEach { it.schema = this }
      }

  private fun createSequences(sequences: List<SequenceDefinition>) = sequences.map {
    createSequence(it)
  }

  private fun createSequence(def: SequenceDefinition) =
      Sequence(
          definition = def,
          name = def.name,
          javaType = getCatalogGeneratorContext().resolveJavaType(def.type),
          sqlType = getCatalogGeneratorContext().resolveSqlType(def.type)
      )

  private fun createForeignKeys(schema: Schema) {
    val uniqKeys = schema.tables.flatMap { it.uniqKeys }
    schema.tables.forEach { it.foreignKeys = createForeignKeys(it, uniqKeys) }
  }

  private fun createTables(tables: List<TableDefinition>) = tables.map { createTable(it) }

  private fun createTable(def: TableDefinition) =
      Table(
          definition = def,
          name = def.name,
          identity = def.identity.mapNotNull { createIdentity(it) },
          uniqKeys = createUniqKeys(def.uniqueKeys),
          columns = createColumns(def.columns),
          simpleRecordClassName = getTableRecordGeneratorCtx().resolveSimpleClassName(def),
          canonicalRecordClassName = getTableRecordGeneratorCtx().resolveCanonicalClassName(def),
          simpleColumnsClassName = getTableColumnsGeneratorCtx().resolveSimpleClassName(def),
          canonicalColumnsClassName = getTableColumnsGeneratorCtx().resolveCanonicalClassName(def),
          simpleTableClassName = getTableGeneratorCtx().resolveSimpleClassName(def),
          canonicalTableClassName = getTableGeneratorCtx().resolveCanonicalClassName(def)
      ).apply {
        columns.forEach { it.table = this }
        identity?.table = this
        uniqKeys.forEach { it.table = this }
        primaryKey = uniqKeys.filter { it.isPrimaryKey }.firstOrNull()

        indexes = createIndexes(def.indexes, columns)
        indexes.forEach { it.table = this }
      }

  private fun createIndexes(indexes: List<IndexDefinition>, columns: List<Column>) =
      indexes.map { createIndex(it, columns) }

  private fun createIndex(def: IndexDefinition, columns: List<Column>) =
      Index(
          definition = def,
          name = def.name,
          columns = def.indexColumns.map({ it.column }).map { filterByName(columns, it.name) },
          isUnique = def.isUnique
      )

  private fun filterByName(columns: List<Column>, name: String) =
      columns.first({ it.name == name })

  private fun createForeignKeys(table: Table, uniqKeys: List<UniqKey>) =
      table.definition.foreignKeys.map { createForeignKey(it, table, uniqKeys) }

  private fun createForeignKey(def: ForeignKeyDefinition, table: Table, uniqKeys: List<UniqKey>) =
      ForeignKey(
          definition = def,
          name = def.name,
          table = table,
          refKey = uniqKeys.filter { def.referencedKey.name == it.name }.first(),
          columns = createColumns(def.keyColumns)
      )

  private fun createUniqKeys(def: List<UniqueKeyDefinition>) = def.map { createUniqKey(it) }

  private fun createUniqKey(def: UniqueKeyDefinition) =
      UniqKey(
          definition = def,
          name = def.name,
          isPrimaryKey = def.isPrimaryKey,
          columns = createColumns(def.keyColumns)
      )

  private fun createColumns(def: List<ColumnDefinition>) = def.map { createColumn(it) }

  private fun createColumn(def: ColumnDefinition) =
      Column(
          definition = def,
          name = def.name,
          sqlType = getTableGeneratorCtx().resolveSqlType(def.type),
          javaName = getTableGeneratorCtx().resolveFieldName(def),
          javaType = getTableGeneratorCtx().resolveJavaType(def.type),
          setterName = getTableGeneratorCtx().resolveSetterName(def),
          getterName = getTableGeneratorCtx().resolveGetterName(def)
      )

  private fun createIdentity(def: IdentityDefinition) =
      Identity(
          definition = def,
          name = def.name,
          column = createColumn(def.column)
      )

  private fun getContext(name: String) = subGenerators.find { it.config.generatorName == name }!!
  private fun getTableGeneratorCtx() = getContext(GeneratorsNames.TABLE)
  private fun getTableRecordGeneratorCtx() = getContext(GeneratorsNames.TABLE_RECORD)
  private fun getSchemaContext() = getContext(GeneratorsNames.SCHEMA)
  private fun getCatalogGeneratorContext() = getContext(GeneratorsNames.CATALOG)
  private fun getSchemaKeysContext() = getContext(GeneratorsNames.SCHEMA_KEYS)
  private fun getSchemaSequencesContext() = getContext(GeneratorsNames.SCHEMA_SEQUENCES)
  private fun getSchemaIndexesContext() = getContext(GeneratorsNames.SCHEMA_INDEXES)
  private fun getTableColumnsGeneratorCtx() = getContext(GeneratorsNames.TABLE_COLUMNS)

}

