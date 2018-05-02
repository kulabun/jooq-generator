package org.labun.jooq.generator

import org.apache.velocity.app.Velocity
import org.apache.velocity.runtime.RuntimeConstants
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader
import org.jooq.util.Database
import org.jooq.util.Definition
import org.jooq.util.SchemaDefinition
import org.labun.jooq.generator.config.Configuration
import org.labun.jooq.generator.config.SubGeneratorConfig
import org.labun.jooq.generator.task.ModelCreator
import org.labun.jooq.generator.task.SubGenerator
import org.labun.jooq.generator.task.SubGeneratorContext
import org.labun.jooq.generator.task.domain.Catalog
import org.labun.jooq.generator.task.domain.Model
import org.labun.jooq.generator.task.domain.Schema
import org.labun.jooq.generator.task.domain.Table
import org.labun.jooq.generator.util.FilePathResolver
import org.labun.jooq.generator.util.InstantiationService
import kotlin.reflect.KClass

/**
 * @author Konstantin Labun
 */
class DefaultGenerator(
    override val configuration: Configuration = Defaults.defaultConfiguration()
) : Generator {

  private val instantiator: InstantiationService = InstantiationService(configuration)
  private val subGenerators: List<SubGenerator<out Model<out Definition>>>
  private val modelCreator: ModelCreator

  init {
    val subGeneratorContexts = configuration.subGenerators.map({ createSubGeneratorContext(it) })
    this.modelCreator = ModelCreator(
        subGenerators = subGeneratorContexts,
        schemaFilter = schemaFilter()
    )
    this.subGenerators = subGeneratorContexts.map({ createSubGenerator(it) })
  }

  private fun createSubGeneratorContext(generatorConfig: SubGeneratorConfig) =
      SubGeneratorContext(
          config = generatorConfig,
          nameCreator = instantiator.createOrGetInstance(generatorConfig.nameCreator),
          sqlTypeResolver = instantiator.createOrGetInstance(generatorConfig.sqlTypeResolver),
          javaTypeResolver = instantiator.createOrGetInstance(generatorConfig.javaTypeResolver),
          filePathResolver = FilePathResolver(generatorConfig)
      )

  private fun createSubGenerator(ctx: SubGeneratorContext) =
      Class.forName(ctx.config.subGenerator)
          .getConstructor(SubGeneratorContext::class.java)
          .newInstance(ctx) as SubGenerator<out Model<out Definition>>

  override fun generate(db: Database) {
    initVelocity()

    db.catalogs.forEach { catalogDefinition ->
      val catalog = modelCreator.create(catalogDefinition)
      catalogTasks().forEach { it.generate(catalog) }

      catalog.schemas.forEach { schema ->
        // run schema generators
        schemaTasks().forEach { it.generate(schema) }
        // run table generators
        schema.tables.forEach { table -> tableTasks().forEach { it.generate(table) } }
      }
    }
  }

  private fun schemaFilter() = { it: SchemaDefinition ->
    configuration.database.schemas.contains(it.name)
  }

  private fun initVelocity() {
    Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "class")
    Velocity.setProperty("class.resource.loader.class",
        ClasspathResourceLoader::class.java.canonicalName)
    Velocity.init()
  }

  private fun tableTasks() = tasksBySupportedType(Table::class) as List<SubGenerator<Table>>
  private fun schemaTasks() = tasksBySupportedType(Schema::class) as List<SubGenerator<Schema>>
  private fun catalogTasks() = tasksBySupportedType(Catalog::class) as List<SubGenerator<Catalog>>
  private fun tasksBySupportedType(type: KClass<out Model<out Definition>>) =
      subGenerators.filter { it.support(type.java) }
}
