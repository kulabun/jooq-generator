package org.labun.jooq.generator.task

import org.apache.velocity.VelocityContext
import org.jooq.util.Definition
import org.labun.jooq.generator.generation.TemplateVariables
import org.labun.jooq.generator.task.domain.Model
import org.labun.jooq.generator.task.domain.Table

/**
 * @author Konstantin Labun
 */
class TableSubGenerator(ctx: SubGeneratorContext) : AbstractSubGenerator<Table>(ctx) {

  override fun configureContext(model: Table, context: VelocityContext): VelocityContext {
    context.put(TemplateVariables.SCHEMA, model.schema)
    context.put(TemplateVariables.TABLE, model)
    context.put(TemplateVariables.PRIMARY_KEY, model.primaryKey)
    context.put(TemplateVariables.UNIQ_KEYS, model.uniqKeys)
    context.put(TemplateVariables.FOREIGN_KEYS, model.foreignKeys)

    context.put(TemplateVariables.COLUMNS, model.columns)

    return context
  }

  override fun support(clazz: Class<out Model<out Definition>>) =
      Table::class.java.isAssignableFrom(clazz)

}
