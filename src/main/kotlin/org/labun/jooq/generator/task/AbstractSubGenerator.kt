package org.labun.jooq.generator.task

import org.apache.velocity.Template
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import org.jooq.util.Definition
import org.labun.jooq.generator.generation.TemplateVariables
import org.labun.jooq.generator.task.domain.Model
import org.labun.jooq.generator.util.TemplateFunctions
import java.nio.file.Files
import java.nio.file.Path

/**
 * @author Konstantin Labun
 */
abstract class AbstractSubGenerator<T : Model<out Definition>>(
    protected var ctx: SubGeneratorContext
) : SubGenerator<T> {

  private var template: Template? = null

  override fun generate(model: T) {
    val context = createContext(model)
    write(context, getFilePath(model))
  }

  private fun createContext(model: T): VelocityContext =
      configureContext(model,
          VelocityContext(hashMapOf<String, Any>(
              Pair(TemplateVariables.PACKAGE, ctx.config.packageName),
              Pair(TemplateVariables.CLASS_NAME, ctx.resolveSimpleClassName(model.definition)),
              Pair(TemplateVariables.UTIL, TemplateFunctions::class)
          )))

  protected abstract fun configureContext(model: T, context: VelocityContext): VelocityContext

  private fun getFilePath(t: T) =
      createDirs(ctx.filePathResolver.resolveFilePath(generateClassName(t)))

  private fun createDirs(path: Path): Path {
    if (!Files.exists(path.parent)) {
      Files.createDirectories(path.parent)
    }
    return path
  }

  private fun generateClassName(t: T) = ctx.nameCreator.createClassName(ctx.config, t.definition)

  private fun write(context: VelocityContext, path: Path) {
    val template = getTemplate(ctx.config.template)
    Files.newBufferedWriter(path).use { writer -> template!!.merge(context, writer) }
  }

  private fun getTemplate(templatePath: String): Template? =
      template ?: Velocity.getTemplate(templatePath)
}