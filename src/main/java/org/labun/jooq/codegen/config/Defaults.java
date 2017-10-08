package org.labun.jooq.codegen.config;

import org.labun.jooq.codegen.task.SchemaCodeGenerationTask;
import org.labun.jooq.codegen.task.TableCodeGenerationTask;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Konstantin Labun
 */
public class Defaults {

    public static class GeneratorsNames {
        public static final String SCHEMA = "SCHEMA";
        public static final String TABLE = "TABLE";
        public static final String RECORD = "RECORD";
        public static final String COLUMNS = "COLUMNS";

        private GeneratorsNames() {
        }
    }

    public static class CodeGenerationConfigs {
        public static final CodeGenerationConfig TABLE_GENERATION_CONFIG = new CodeGenerationConfig()
                .codeGenerationTask(TableCodeGenerationTask.class.getCanonicalName())
                .generatorName(GeneratorsNames.TABLE)
                .packageName("org.labun.jooq.codegen.table")
                .className(new NameConfig().postfix("Table"))
                .template("templates/java/table.vm")
                .javaTimeDates(true);

        public static final CodeGenerationConfig RECORD_GENERATION_CONFIG = new CodeGenerationConfig()
                .codeGenerationTask(TableCodeGenerationTask.class.getCanonicalName())
                .generatorName(GeneratorsNames.RECORD)
                .packageName("org.labun.jooq.codegen.record")
                .className(new NameConfig().postfix("Record"))
                .template("templates/java/record.vm")
                .javaTimeDates(true);

        public static final CodeGenerationConfig COLUMNS_GENERATION_CONFIG = new CodeGenerationConfig()
                .codeGenerationTask(TableCodeGenerationTask.class.getCanonicalName())
                .generatorName(GeneratorsNames.COLUMNS)
                .packageName("org.labun.jooq.codegen.column")
                .className(new NameConfig().postfix("Columns"))
                .template("templates/java/columns.vm")
                .javaTimeDates(true);

        public static final CodeGenerationConfig SCHEMA_GENERATION_CONFIG = new CodeGenerationConfig()
                .codeGenerationTask(SchemaCodeGenerationTask.class.getCanonicalName())
                .generatorName(GeneratorsNames.SCHEMA)
                .packageName("org.labun.jooq.codegen")
                .className(new NameConfig())
                .template("templates/java/schema.vm");

        private CodeGenerationConfigs() {
        }
    }

    public static class TemplateVariables {
        public static final String SCHEMA = "schema";
        public static final String TABLE = "table";
        public static final String COLUMNS = "columns";
        public static final String PACKAGE = "package";
        public static final String CLASS_NAME = "className";
        public static final String COLUMNS_CLASS_NAME = "columnsClassName";
        public static final String TABLE_CLASS_NAME = "tableClassName";
        public static final String RECORD_CLASS_NAME = "recordClassName";

        private TemplateVariables() {
        }
    }

    public static Configuration configuration() {
        return new Configuration()
                .codeGeneration(new ArrayList<>(Arrays.asList(
                        CodeGenerationConfigs.COLUMNS_GENERATION_CONFIG,
                        CodeGenerationConfigs.RECORD_GENERATION_CONFIG,
                        CodeGenerationConfigs.TABLE_GENERATION_CONFIG,
                        CodeGenerationConfigs.SCHEMA_GENERATION_CONFIG
                )));
    }

    private Defaults() {
    }
}
