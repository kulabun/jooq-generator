package org.labun.jooq.generator

import org.jooq.util.Database
import org.labun.jooq.generator.config.Configuration

/**
 * @author Konstantin Labun
 */
interface Generator {

  val configuration: Configuration

  fun generate(db: Database)
}