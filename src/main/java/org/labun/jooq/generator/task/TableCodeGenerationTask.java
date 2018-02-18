package org.labun.jooq.generator.task;

import lombok.Data;
import org.apache.velocity.VelocityContext;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.UniqueKeyDefinition;
import org.labun.jooq.generator.config.Defaults;
import org.labun.jooq.generator.config.Defaults.TemplateVariables;

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
        context.put(TemplateVariables.ID, getId(table));
        context.put(TemplateVariables.UNIQ_KEYS, getUniqKeys(table));
        context.put(TemplateVariables.COLUMNS, getColumns(table));
        context.put(TemplateVariables.COLUMNS_CLASS_NAME, getClassName(table, getTaskContext(Defaults.GeneratorsNames.COLUMNS)));
        context.put(TemplateVariables.RECORD_CLASS_NAME, getClassName(table, getTaskContext(Defaults.GeneratorsNames.RECORD)));
        context.put(TemplateVariables.TABLE_CLASS_NAME, getClassName(table, getTaskContext(Defaults.GeneratorsNames.TABLE)));

        return context;
    }

    private List<UniqKey> getUniqKeys(TableDefinition table) {
        return table.getUniqueKeys()
                .stream()
                .map(key -> toUniqKey(key))
                .collect(Collectors.toList());
    }

    private UniqKey toUniqKey(UniqueKeyDefinition key) {
        List<Column> columns = key.getKeyColumns().stream()
                .map(it -> toColumn(it))
                .collect(Collectors.toList());

        return new UniqKey()
                .name(key.getName())
                .columns(columns);
    }

    private Id getId(TableDefinition table) {
        List<Column> columns = table.getColumns().stream()
                .filter(it -> it.isIdentity())
                .map(it -> toColumn(it)
                ).collect(Collectors.toList());
        return (Id) new Id().columns(columns);
    }

    private Column toColumn(ColumnDefinition it) {
        return new Column()
                .name(it.getName())
                .sqlType(resolveSqlType(it))
                .javaName(resolveFieldName(it))
                .javaType(resolveJavaType(it))
                .setterName(resolveSetterName(it))
                .getterName(resolveGetterName(it));
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
                .map(it -> toColumn(it)
                ).collect(Collectors.toList());
    }

    private String resolveSetterName(ColumnDefinition columnDefinition) {
        return ctx.nameCreator().createSetterName(ctx.config(), columnDefinition);
    }

    private String resolveGetterName(ColumnDefinition columnDefinition) {
        return ctx.nameCreator().createGetterName(ctx.config(), columnDefinition);
    }

    private String resolveFieldName(ColumnDefinition it) {
        return ctx.nameCreator().createFieldName(ctx.config(), it);
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

    @Data
    public static class Column {
        private String name;
        private String sqlType;
        private String javaName;
        private String javaType;
        private String setterName;
        private String getterName;

        public String getName() {
            return name;
        }

        public String getJavaName() {
            return javaName;
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

    @Data
    public static class UniqKey {
        private String name;
        private List<Column> columns;

        public String getName() {
            return name;
        }

        public List<Column> getColumns() {
            return columns;
        }
    }

    @Data
    public static class Id extends UniqKey {
    }
}
