package org.labun.jooq.generator.task

import java.util.stream.Collectors
import org.apache.velocity.VelocityContext
import org.jooq.util.TableDefinition
import org.labun.jooq.generator.generation.GeneratorsNames
import org.labun.jooq.generator.generation.TemplateVariables
import org.labun.jooq.generator.task.domain.Column

/**
 * @author Konstantin Labun
 */
class TableCodeGenerationTask(ctx: TaskContext) : AbstractCodeGenerationTask<TableDefinition>(ctx) {

    override fun configureContext(table: TableDefinition, context: VelocityContext): VelocityContext {
        var context = context
        context = super.configureContext(table, context)

        context.put(TemplateVariables.SCHEMA, table.schema)
        context.put(TemplateVariables.TABLE, table)
        context.put(TemplateVariables.ID, getId(table))
        context.put(TemplateVariables.UNIQ_KEYS, getUniqKeys(table))
        context.put(TemplateVariables.COLUMNS, getColumns(table))
        context.put(TemplateVariables.COLUMNS_CLASS_NAME, getClassName(table, getTaskContext(GeneratorsNames.COLUMNS)))
        context.put(TemplateVariables.RECORD_CLASS_NAME, getClassName(table, getTaskContext(GeneratorsNames.RECORD)))
        context.put(TemplateVariables.SCHEMA_CLASS_NAME, getClassName(table.schema, getTaskContext(GeneratorsNames.SCHEMA)))
        context.put(TemplateVariables.TABLE_CLASS_NAME, getClassName(table, getTaskContext(GeneratorsNames.TABLE)))

        return context
    }

    private fun getColumns(table: TableDefinition): List<Column> {
        return table.columns.stream()
                .map<Any> { it -> toColumn(it) }
                .collect<List<Column>, Any>(Collectors.toList<Any>())
    }

    override fun support(clazz: Class<TableDefinition>): Boolean {
        return TableDefinition::class.java.isAssignableFrom(clazz)
    }

}
