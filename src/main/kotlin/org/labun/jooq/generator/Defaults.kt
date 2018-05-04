package org.labun.jooq.generator

import org.labun.jooq.generator.config.Configuration
import org.labun.jooq.generator.config.NameConfig
import org.labun.jooq.generator.config.SubGeneratorConfig
import org.labun.jooq.generator.generation.GeneratorsNames
import org.labun.jooq.generator.task.CatalogSubGenerator
import org.labun.jooq.generator.task.SchemaSubGenerator
import org.labun.jooq.generator.task.TableSubGenerator

/**
 * @author Konstantin Labun
 */
object Defaults {

  @JvmStatic fun defaultConfiguration(): Configuration {
    return Configuration(
        subGenerators = listOf(
            CodeGenerationConfigs.CATALOG_GENERATION_CONFIG,
            CodeGenerationConfigs.SCHEMA_GENERATION_CONFIG,
            CodeGenerationConfigs.SEQUENCES_GENERATION_CONFIG,
            CodeGenerationConfigs.INDEXES_GENERATION_CONFIG,
            CodeGenerationConfigs.TABLE_GENERATION_CONFIG,
            CodeGenerationConfigs.RECORD_GENERATION_CONFIG,
            CodeGenerationConfigs.COLUMNS_GENERATION_CONFIG,
            CodeGenerationConfigs.KEYS_GENERATION_CONFIG,
            CodeGenerationConfigs.REPOSITORY_GENERATION_CONFIG
        ))
  }

  object CodeGenerationConfigs {

    val CATALOG_GENERATION_CONFIG = catalogGenerationConfig()
    val SCHEMA_GENERATION_CONFIG = schemaGenerationConfig()
    val SEQUENCES_GENERATION_CONFIG = sequenceGenerationConfig()
    val INDEXES_GENERATION_CONFIG = indexesGenerationConfig()
    val TABLE_GENERATION_CONFIG = tableGenerationConfig()
    val RECORD_GENERATION_CONFIG = recordGenerationConfig()
    val COLUMNS_GENERATION_CONFIG = columnsGenerationConfig()
    val KEYS_GENERATION_CONFIG = keysGenerationConfig()
    val REPOSITORY_GENERATION_CONFIG = repositoryGenerationConfig()

    private fun catalogGenerationConfig() = SubGeneratorConfig(
        generatorName = GeneratorsNames.CATALOG,
        subGenerator = CatalogSubGenerator::class.java.canonicalName,
        packageName = "org.labun.jooq.subgenerators",
        className = NameConfig(postfix = "Catalog"),
        template = "templates/java/catalog.vm"
    )

    private fun schemaGenerationConfig() =
        SubGeneratorConfig(
            generatorName = GeneratorsNames.SCHEMA,
            subGenerator = SchemaSubGenerator::class.java.canonicalName,
            packageName = "org.labun.jooq.subgenerators",
            className = NameConfig(),
            template = "templates/java/schema.vm"
        )

    private fun sequenceGenerationConfig() =
        SubGeneratorConfig(
            generatorName = GeneratorsNames.SCHEMA_SEQUENCES,
            subGenerator = SchemaSubGenerator::class.java.canonicalName,
            packageName = "org.labun.jooq.subgenerators",
            className = NameConfig(postfix = "Sequences"),
            template = "templates/java/sequences.vm"
        )

    private fun indexesGenerationConfig() =
        SubGeneratorConfig(
            generatorName = GeneratorsNames.SCHEMA_INDEXES,
            subGenerator = SchemaSubGenerator::class.java.canonicalName,
            packageName = "org.labun.jooq.subgenerators",
            className = NameConfig(postfix = "Indexes"),
            template = "templates/java/indexes.vm"
        )

    private fun tableGenerationConfig() =
        SubGeneratorConfig(
            subGenerator = TableSubGenerator::class.java.canonicalName,
            generatorName = GeneratorsNames.TABLE,
            packageName = "org.labun.jooq.subgenerators.table",
            className = NameConfig(postfix = "Table"),
            template = "templates/java/table.vm",
            javaTimeDates = true
        )

    private fun recordGenerationConfig(): SubGeneratorConfig {
      val subGeneratorConfig = SubGeneratorConfig(
          subGenerator = TableSubGenerator::class.java.canonicalName,
          generatorName = GeneratorsNames.TABLE_RECORD,
          packageName = "org.labun.jooq.subgenerators.record",
          className = NameConfig(postfix = "Record"),
          template = "templates/java/record.vm",
          javaTimeDates = true
      )
      return subGeneratorConfig
    }

    private fun columnsGenerationConfig() =
        SubGeneratorConfig(
            subGenerator = TableSubGenerator::class.java.canonicalName,
            generatorName = GeneratorsNames.TABLE_COLUMNS,
            packageName = "org.labun.jooq.subgenerators.column",
            className = NameConfig(postfix = "Columns"),
            template = "templates/java/columns.vm",
            javaTimeDates = true
        )

    private fun keysGenerationConfig() =
        SubGeneratorConfig(
            subGenerator = SchemaSubGenerator::class.java.canonicalName,
            generatorName = GeneratorsNames.SCHEMA_KEYS,
            packageName = "org.labun.jooq.subgenerators",
            className = NameConfig(postfix = "Keys"),
            template = "templates/java/keys.vm"
        )

    private fun repositoryGenerationConfig(): SubGeneratorConfig {
      return SubGeneratorConfig(
          subGenerator = TableSubGenerator::class.java.canonicalName,
          generatorName = GeneratorsNames.REPOSITORY,
          packageName = "org.labun.jooq.subgenerators.repository",
          className = NameConfig(postfix = "RepositoryBase"),
          template = "templates/java/repository.vm",
          javaTimeDates = true
      )
    }

    @JvmStatic fun all(): List<SubGeneratorConfig> = listOf(
        schemaGenerationConfig(),
        sequenceGenerationConfig(),
        indexesGenerationConfig(),
        keysGenerationConfig(),
        tableGenerationConfig(),
        recordGenerationConfig(),
        columnsGenerationConfig(),
        repositoryGenerationConfig(),
        catalogGenerationConfig())

    @JvmStatic fun base(): List<SubGeneratorConfig> = listOf(
        schemaGenerationConfig(),
        sequenceGenerationConfig(),
        indexesGenerationConfig(),
        keysGenerationConfig(),
        tableGenerationConfig(),
        recordGenerationConfig(),
        columnsGenerationConfig(),
        catalogGenerationConfig())
  }

}