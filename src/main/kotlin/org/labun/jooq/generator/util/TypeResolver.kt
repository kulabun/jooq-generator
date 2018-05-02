package org.labun.jooq.generator.util

import org.jooq.util.ColumnDefinition
import org.jooq.util.DataTypeDefinition
import org.labun.jooq.generator.task.SubGeneratorContext

/**
 * @author Konstantin Labun
 */
interface TypeResolver {

  fun resolve(context: SubGeneratorContext, def: DataTypeDefinition): String
}
