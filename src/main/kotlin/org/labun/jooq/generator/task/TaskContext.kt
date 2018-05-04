package org.labun.jooq.generator.task

import lombok.Getter
import lombok.Setter
import org.labun.jooq.generator.GeneratorContext
import org.labun.jooq.generator.config.CodeGenerationConfig

/*
 * @author Konstantin Labun
 */
@Getter
@Setter
class TaskContext {

    private val generatorContext: GeneratorContext? = null
    private val config: CodeGenerationConfig? = null
    private val nameCreator: NameCreator? = null
    private val sqlTypeResolver: TypeResolver? = null
    private val javaTypeResolver: TypeResolver? = null
    private val filePathResolver: FilePathResolver? = null
}
