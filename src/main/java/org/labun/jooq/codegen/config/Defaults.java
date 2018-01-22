package org.labun.jooq.codegen.config;

import org.labun.jooq.codegen.task.SchemaCodeGenerationTask;
import org.labun.jooq.codegen.task.TableCodeGenerationTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Konstantin Labun
 */
public class Defaults {

    private Defaults() {
    }

    public static Configuration configuration() {
        return new Configuration()
                .codeGeneration(new ArrayList<>(Arrays.asList(
                        CodeGenerationConfigs.COLUMNS_GENERATION_CONFIG,
                        CodeGenerationConfigs.RECORD_GENERATION_CONFIG,
                        CodeGenerationConfigs.TABLE_GENERATION_CONFIG,
                        CodeGenerationConfigs.SCHEMA_GENERATION_CONFIG,
                        CodeGenerationConfigs.REPOSITORY_GENERATION_CONFIG
                )));
    }

    public static class GeneratorsNames {
        public static final String SCHEMA = "DEFAULT:SCHEMA";
        public static final String TABLE = "DEFAULT:TABLE";
        public static final String RECORD = "DEFAULT:RECORD";
        public static final String COLUMNS = "DEFAULT:COLUMNS";
        public static final String REPOSITORY = "DEFAULT:REPOSITORY";

        private GeneratorsNames() {
        }
    }

    public static class CodeGenerationConfigs {
        public static final CodeGenerationConfig TABLE_GENERATION_CONFIG = tableGenerationConfig();
        public static final CodeGenerationConfig RECORD_GENERATION_CONFIG = recordGenerationConfig();
        public static final CodeGenerationConfig COLUMNS_GENERATION_CONFIG = columnsGenerationConfig();
        public static final CodeGenerationConfig SCHEMA_GENERATION_CONFIG = schemaGenerationConfig();
        public static final CodeGenerationConfig REPOSITORY_GENERATION_CONFIG = repositoryGenerationConfig();

        private CodeGenerationConfigs() {
        }

        private static CodeGenerationConfig schemaGenerationConfig() {
            return new CodeGenerationConfig()
                    .codeGenerationTask(SchemaCodeGenerationTask.class.getCanonicalName())
                    .generatorName(GeneratorsNames.SCHEMA)
                    .packageName("org.labun.jooq.codegen")
                    .className(new NameConfig())
                    .template("templates/java/schema.vm");
        }

        private static CodeGenerationConfig columnsGenerationConfig() {
            return new CodeGenerationConfig()
                    .codeGenerationTask(TableCodeGenerationTask.class.getCanonicalName())
                    .generatorName(GeneratorsNames.COLUMNS)
                    .packageName("org.labun.jooq.codegen.column")
                    .className(new NameConfig().postfix("Columns"))
                    .template("templates/java/columns.vm")
                    .javaTimeDates(true);
        }

        private static CodeGenerationConfig recordGenerationConfig() {
            return new CodeGenerationConfig()
                    .codeGenerationTask(TableCodeGenerationTask.class.getCanonicalName())
                    .generatorName(GeneratorsNames.RECORD)
                    .packageName("org.labun.jooq.codegen.record")
                    .className(new NameConfig().postfix("Record"))
                    .template("templates/java/record.vm")
                    .javaTimeDates(true);
        }

        private static CodeGenerationConfig tableGenerationConfig() {
            return new CodeGenerationConfig()
                    .codeGenerationTask(TableCodeGenerationTask.class.getCanonicalName())
                    .generatorName(GeneratorsNames.TABLE)
                    .packageName("org.labun.jooq.codegen.table")
                    .className(new NameConfig().postfix("Table"))
                    .template("templates/java/table.vm")
                    .javaTimeDates(true);
        }

        private static CodeGenerationConfig repositoryGenerationConfig() {
            return new CodeGenerationConfig()
                    .codeGenerationTask(TableCodeGenerationTask.class.getCanonicalName())
                    .generatorName(GeneratorsNames.REPOSITORY)
                    .packageName("org.labun.jooq.codegen.repository")
                    .className(new NameConfig().postfix("RepositoryBase"))
                    .template("templates/java/repository.vm")
                    .javaTimeDates(true);
        }

        public static List<CodeGenerationConfig> all() {
            return Arrays.asList(
                    tableGenerationConfig(),
                    recordGenerationConfig(),
                    columnsGenerationConfig(),
                    schemaGenerationConfig(),
                    repositoryGenerationConfig());
        }
    }

    public static class TemplateVariables {
        public static final String SCHEMA = "schema";
        public static final String TABLE = "table";
        public static final String ID = "id";
        public static final String COLUMNS = "columns";
        public static final String PACKAGE = "package";
        public static final String CLASS_NAME = "className";
        public static final String COLUMNS_CLASS_NAME = "columnsClassName";
        public static final String TABLE_CLASS_NAME = "tableClassName";
        public static final String RECORD_CLASS_NAME = "recordClassName";
        public static final String UNIQ_KEYS = "uniqKeys";

        private TemplateVariables() {
        }
    }
}
