package org.labun.jooq.codegen.task;

import lombok.Getter;
import lombok.Setter;
import org.labun.jooq.codegen.GeneratorContext;
import org.labun.jooq.codegen.config.CodeGenerationConfig;
import org.labun.jooq.codegen.util.FilePathResolver;
import org.labun.jooq.codegen.util.NameCreator;
import org.labun.jooq.codegen.util.TypeResolver;

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
