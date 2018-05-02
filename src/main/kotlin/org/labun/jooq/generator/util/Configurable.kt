package org.labun.jooq.generator.util

import org.labun.jooq.generator.config.Configuration


/**
 * @author Konstantin Labun
 */
interface Configurable {

  fun configure(configuration: Configuration)
}
