package org.labun.jooq.generator.task

import java.util.stream.Collectors
import org.apache.velocity.VelocityContext
import org.jooq.util.SchemaDefinition
import org.labun.jooq.generator.generation.TemplateVariables
import org.labun.jooq.generator.task.domain.Schema

/**
 * @author Konstantin Labun
 */
class SchemaCodeGenerationTask(ctx: TaskContext) : AbstractCodeGenerationTask<SchemaDefinition>(ctx) {

    protected fun configureContext(schema: Schema,
                                   context: VelocityContext): VelocityContext {
        var context = context
        context = super.configureContext(schema, context)
        context.put(TemplateVariables.SCHEMA, schema)
        context.put(TemplateVariables.IDENTITIES, getIdentities(schema))
        //        context.put(Defaults.TemplateVariables.PRIMARY_KEYS, getUniqKeys(schemaDefinition));
        //        context.put(Defaults.TemplateVariables.INDEXES, getUniqKeys(schemaDefinition));
        //        context.put(Defaults.TemplateVariables.FOREIGN_KEYS, getUniqKeys(schemaDefinition));
        //        schemaDefinition.getCatalog().getDatabase().getDialect();
        return context
    }

    private fun getIdentities(schema: Schema): Any {
        return schema.tables().stream()
                .map({ it -> it.identity() })
                .collect(Collectors.toList<T>())
    }

    override fun support(clazz: Class<SchemaDefinition>): Boolean {
        return SchemaDefinition::class.java.isAssignableFrom(clazz)
    }

}
