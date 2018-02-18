package org.labun.jooq.generator.task;

import org.jooq.util.Definition;

/**
 * @author Konstantin Labun
 */
public interface CodeGenerationTask<T extends Definition> {
    void generate(T def);

    boolean support(Class<T> clazz);
}
