package org.labun.jooq.generator.task

import org.jooq.util.Definition
import org.labun.jooq.generator.task.domain.Model

/**
 * @author Konstantin Labun
 */
interface SubGenerator<T : Model<out Definition>> {

  fun generate(model: T)

  fun support(clazz: Class<out Model<out Definition>>): Boolean
}
