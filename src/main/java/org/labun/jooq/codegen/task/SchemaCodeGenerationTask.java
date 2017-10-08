package org.labun.jooq.codegen.task;

import org.apache.velocity.VelocityContext;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.TableDefinition;
import org.labun.jooq.codegen.config.Defaults;

/**
 * @author Konstantin Labun
 */
public class SchemaCodeGenerationTask extends AbstractCodeGenerationTask<SchemaDefinition> {


    public SchemaCodeGenerationTask(TaskContext ctx) {
        super(ctx);
    }

    @Override
    protected VelocityContext configureContext(SchemaDefinition schemaDefinition, VelocityContext context) {
        context = super.configureContext(schemaDefinition, context);
        context.put(Defaults.TemplateVariables.SCHEMA, schemaDefinition);
//        schemaDefinition.getCatalog().getDatabase().getDialect();
        return context;
    }

    @Override
    public boolean support(Class<SchemaDefinition> clazz) {
        return SchemaDefinition.class.isAssignableFrom(clazz);
    }
}
