package org.labun.jooq.codegen.task;

import org.jooq.util.Definition;

/**
 * @author Konstantin Labun
 */
public interface CodeGenerationTask<T extends Definition> {
    void generate(T def);

    boolean support(Class<T> clazz);
}
