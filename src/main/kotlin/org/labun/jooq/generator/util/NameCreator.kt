package org.labun.jooq.generator.util

import org.jooq.util.Definition
import org.labun.jooq.generator.config.SubGeneratorConfig

/**
 * @author Konstantin Labun
 */
interface NameCreator {

  fun createClassName(cfg: SubGeneratorConfig, table: Definition): String

  fun createFieldName(cfg: SubGeneratorConfig, column: Definition): String

  fun createGetterName(cfg: SubGeneratorConfig, column: Definition): String

  fun createSetterName(cfg: SubGeneratorConfig, column: Definition): String

}
