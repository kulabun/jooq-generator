package org.labun.jooq.codegen.util;

import org.jooq.util.Definition;
import org.labun.jooq.codegen.config.CodeGenerationConfig;

/**
 * @author Konstantin Labun
 */
public interface NameCreator {
    String createClassName(CodeGenerationConfig cfg, Definition table);

    String createGetterName(CodeGenerationConfig cfg, Definition column);

    String createSetterName(CodeGenerationConfig cfg, Definition column);

}
