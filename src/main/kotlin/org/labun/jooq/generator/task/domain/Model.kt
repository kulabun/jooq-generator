package org.labun.jooq.generator.task.domain

import org.jooq.util.Definition

/**
 * @author Konstantin Labun
 */
interface Model<T : Definition> {

  val definition: T
}