package org.labun.jooq.generator.task

import org.apache.velocity.VelocityContext
import org.jooq.util.Definition
import org.labun.jooq.generator.generation.TemplateVariables
import org.labun.jooq.generator.task.domain.Catalog
import org.labun.jooq.generator.task.domain.Model

/**
 * @author Konstantin Labun
 */
class CatalogSubGenerator(ctx: SubGeneratorContext) : AbstractSubGenerator<Catalog>(ctx) {

  override fun configureContext(model: Catalog, context: VelocityContext): VelocityContext {
    context.put(TemplateVariables.CATALOG, model)
    context.put(TemplateVariables.SCHEMAS, model.schemas)

    return context
  }

  override fun support(clazz: Class<out Model<out Definition>>) =
      Catalog::class.java.isAssignableFrom(clazz)
}
