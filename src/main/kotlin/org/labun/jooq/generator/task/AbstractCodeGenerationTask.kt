package org.labun.jooq.generator.task

import java.io.BufferedWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors
import lombok.SneakyThrows
import org.apache.velocity.Template
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import org.jooq.util.Definition
import org.jooq.util.SchemaDefinition
import org.jooq.util.TableDefinition
import org.jooq.util.UniqueKeyDefinition
import org.labun.jooq.generator.generation.TemplateVariables
import org.labun.jooq.generator.task.domain.Column
import org.labun.jooq.generator.task.domain.PrimaryKey
import org.labun.jooq.generator.task.domain.UniqKey

/**
 * @author Konstantin Labun
 */
abstract class AbstractCodeGenerationTask<T : Definition>(protected var ctx: TaskContext) : CodeGenerationTask<T> {
    private var template: Template? = null

    @SneakyThrows
    override fun generate(t: T) {
        val context = createContext(t)
        write(context, getFilePath(t))
    }

    private fun createContext(t: T): VelocityContext {
        return configureContext(t, VelocityContext())
    }

    protected open fun configureContext(t: T, context: VelocityContext): VelocityContext {
        context.put(TemplateVariables.PACKAGE, ctx.config()!!.packageName())
        context.put(TemplateVariables.CLASS_NAME, generateClassName(t))
        context.put("util", TemplateFunctions::class.java)
        return context
    }

    private fun getFilePath(t: T): Path {
        return createDirs(ctx.filePathResolver()!!.resolveFilePath(generateClassName(t)))
    }

    @SneakyThrows
    private fun createDirs(path: Path): Path {
        if (!Files.exists(path.parent)) {
            Files.createDirectories(path.parent)
        }
        return path
    }

    private fun generateClassName(t: T): String {
        return ctx.nameCreator()!!.createClassName(ctx.config(), t)
    }

    @Throws(IOException::class)
    private fun write(context: VelocityContext, path: Path) {
        val template = getTemplate(ctx.config()!!.template())
        Files.newBufferedWriter(path).use { writer -> template!!.merge(context, writer) }
    }

    private fun getTemplate(templatePath: String): Template? {
        if (template == null) {
            template = Velocity.getTemplate(templatePath)
        }
        return template
    }

    protected fun getUniqKeys(schema: SchemaDefinition): List<UniqKey> {
        return schema.tables
                .stream()
                .map { table -> getUniqKeys(table) }
                .flatMap { it -> it.stream() }
                .collect<List<UniqKey>, Any>(Collectors.toList())
    }

    protected fun getUniqKeys(table: TableDefinition): List<UniqKey> {
        return table.uniqueKeys
                .stream()
                .map { key -> toUniqKey(key) }
                .collect<List<UniqKey>, Any>(Collectors.toList())
    }

    protected fun toUniqKey(key: UniqueKeyDefinition): UniqKey {
        val columns = key.keyColumns.stream()
                .map<Any> { it -> toColumn(it) }
                .collect<List<Column>, Any>(Collectors.toList<Any>())

        return UniqKey()
                .name(key.name)
                .columns(columns)
    }

    protected fun getId(table: TableDefinition): PrimaryKey {
        val columns = table.columns.stream()
                .filter { it -> it.isIdentity }
                .map<Any> { it -> toColumn(it) }.collect<List<Column>, Any>(Collectors.toList<Any>())
        return PrimaryKey().columns(columns) as PrimaryKey
    }


    protected fun getTaskContext(generatorName: String): TaskContext {
        return ctx.generatorContext()!!.getTaskContextByGeneratorName(generatorName)
    }

}