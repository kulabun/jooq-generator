package org.labun.jooq.generator.task;

import lombok.Getter;
import lombok.Setter;
import org.labun.jooq.generator.GeneratorContext;
import org.labun.jooq.generator.config.CodeGenerationConfig;
import org.labun.jooq.generator.util.FilePathResolver;
import org.labun.jooq.generator.util.NameCreator;
import org.labun.jooq.generator.util.TypeResolver;

/**
 * @author Konstantin Labun
 */
@Getter
@Setter
public class TaskContext {
    private GeneratorContext generatorContext;
    private CodeGenerationConfig config;
    private NameCreator nameCreator;
    private TypeResolver sqlTypeResolver;
    private TypeResolver javaTypeResolver;
    private FilePathResolver filePathResolver;
}
