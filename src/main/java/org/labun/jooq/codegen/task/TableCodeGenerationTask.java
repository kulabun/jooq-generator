package org.labun.jooq.codegen.task;

import lombok.Getter;
import lombok.Setter;
import org.apache.velocity.VelocityContext;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.TableDefinition;
import org.labun.jooq.codegen.config.Defaults;
import org.labun.jooq.codegen.config.Defaults.TemplateVariables;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Konstantin Labun
 */
public class TableCodeGenerationTask extends AbstractCodeGenerationTask<TableDefinition> {

    public TableCodeGenerationTask(TaskContext ctx) {
        super(ctx);
    }

    @Override
    protected VelocityContext configureContext(TableDefinition table, VelocityContext context) {
        context = super.configureContext(table, context);

        context.put(TemplateVariables.SCHEMA, table.getSchema());
        context.put(TemplateVariables.TABLE, table);
        context.put(TemplateVariables.COLUMNS, getColumns(table));
        context.put(TemplateVariables.COLUMNS_CLASS_NAME, getClassName(table, getTaskContext(Defaults.GeneratorsNames.COLUMNS)));
        context.put(TemplateVariables.RECORD_CLASS_NAME, getClassName(table, getTaskContext(Defaults.GeneratorsNames.RECORD)));
        context.put(TemplateVariables.TABLE_CLASS_NAME, getClassName(table, getTaskContext(Defaults.GeneratorsNames.TABLE)));

        return context;
    }

    private String getClassName(TableDefinition table, TaskContext ctx) {
        return String.format("%s.%s", ctx.config().packageName(),
                ctx.nameCreator().createClassName(ctx.config(), table));
    }

    private TaskContext getTaskContext(String generatorName) {
        return ctx.generatorContext().getTaskContextByGeneratorName(generatorName);
    }

    private List<Column> getColumns(TableDefinition table) {
        return table.getColumns().stream()
                .map(it ->
                        new Column()
                                .name(it.getName())
                                .sqlType(resolveSqlType(it))
                                .javaType(resolveJavaType(it))
                                .setterName(resolveSetterName(it))
                                .getterName(resolveGetterName(it))
                ).collect(Collectors.toList());
    }

    private String resolveSetterName(ColumnDefinition columnDefinition) {
        return ctx.nameCreator().createSetterName(ctx.config(), columnDefinition);
    }

    private String resolveGetterName(ColumnDefinition columnDefinition) {
        return ctx.nameCreator().createGetterName(ctx.config(), columnDefinition);
    }

    private String resolveSqlType(ColumnDefinition column) {
        return ctx.sqlTypeResolver().resolve(ctx, column);
    }

    private String resolveJavaType(ColumnDefinition column) {
        return ctx.javaTypeResolver().resolve(ctx, column);
    }


    @Override
    public boolean support(Class<TableDefinition> clazz) {
        return TableDefinition.class.isAssignableFrom(clazz);
    }

    @Getter
    @Setter
    public static class Column {
        private String name;
        private String sqlType;
        private String javaType;
        private String setterName;
        private String getterName;

        public String getName() {
            return name;
        }

        public String getSqlType() {
            return sqlType;
        }

        public String getJavaType() {
            return javaType;
        }

        public String getSetterName() {
            return setterName;
        }

        public String getGetterName() {
            return getterName;
        }
    }
}
