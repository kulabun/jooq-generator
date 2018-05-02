package org.labun.jooq.generator.task

import org.apache.velocity.VelocityContext
import org.jooq.util.Definition
import org.labun.jooq.generator.generation.TemplateVariables
import org.labun.jooq.generator.task.domain.Model
import org.labun.jooq.generator.task.domain.Schema

/**
 * @author Konstantin Labun
 */
class SchemaSubGenerator(ctx: SubGeneratorContext) : AbstractSubGenerator<Schema>(ctx) {

  override fun configureContext(model: Schema, context: VelocityContext): VelocityContext {
    context.put(TemplateVariables.SCHEMA, model)
    context.put(TemplateVariables.IDENTITIES, getIdentities(model))
    context.put(TemplateVariables.UNIQ_KEYS, getUniqKeys(model))
    context.put(TemplateVariables.FOREIGN_KEYS, getForeignKeys(model))
    context.put(TemplateVariables.PRIMARY_KEYS, getPrimaryKeys(model));

    return context
  }

  private fun getPrimaryKeys(schema: Schema) = schema.tables.map { it.primaryKey }.filterNotNull()
  private fun getForeignKeys(schema: Schema) = schema.tables.flatMap { it.foreignKeys }
  private fun getUniqKeys(schema: Schema) = schema.tables.flatMap { it.uniqKeys }
  private fun getIdentities(schema: Schema) = schema.tables.map({ it.identity }).filterNotNull()

  override fun support(clazz: Class<out Model<out Definition>>) =
      Schema::class.java.isAssignableFrom(clazz)
}
