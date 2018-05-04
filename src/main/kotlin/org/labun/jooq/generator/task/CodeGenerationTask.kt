package org.labun.jooq.generator.task

import org.jooq.util.Definition

/**
 * @author Konstantin Labun
 */
interface CodeGenerationTask<T : Definition> {
    fun generate(def: T)

    fun support(clazz: Class<T>): Boolean
}
