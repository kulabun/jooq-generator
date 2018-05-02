package org.labun.jooq.generator.task

import org.jooq.DataType
import org.jooq.util.ColumnDefinition
import org.jooq.util.DataTypeDefinition
import org.jooq.util.Definition
import org.labun.jooq.generator.config.SubGeneratorConfig
import org.labun.jooq.generator.util.FilePathResolver
import org.labun.jooq.generator.util.NameCreator
import org.labun.jooq.generator.util.TypeResolver

/*
 * @author Konstantin Labun
 */
data class SubGeneratorContext(
    val config: SubGeneratorConfig,
    val nameCreator: NameCreator,
    val sqlTypeResolver: TypeResolver,
    val javaTypeResolver: TypeResolver,
    val filePathResolver: FilePathResolver
) {

  fun resolveJavaType(def: DataTypeDefinition) = javaTypeResolver.resolve(this, def)
  fun resolveSqlType(def: DataTypeDefinition) = sqlTypeResolver.resolve(this, def)
  fun resolveSetterName(def: ColumnDefinition) = nameCreator.createSetterName(config, def)
  fun resolveGetterName(def: ColumnDefinition) = nameCreator.createGetterName(config, def)
  fun resolveFieldName(def: ColumnDefinition) = nameCreator.createFieldName(config, def)
  fun resolveSimpleClassName(def: Definition) = nameCreator.createClassName(config, def)
  fun resolveCanonicalClassName(def: Definition) =
      "${config.packageName}.${resolveSimpleClassName(def)}"
}