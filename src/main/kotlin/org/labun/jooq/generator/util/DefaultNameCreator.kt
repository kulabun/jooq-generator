package org.labun.jooq.generator.util

import lombok.Getter
import lombok.Setter
import org.apache.commons.lang3.StringUtils
import org.jooq.util.Definition
import org.labun.jooq.generator.config.SubGeneratorConfig
import org.labun.jooq.generator.config.Configuration

/**
 * @author Konstantin Labun
 */
@Getter
@Setter
class DefaultNameCreator : NameCreator, Configurable {

  private val nameConverter = TableNameConverter()
  private var configuration: Configuration? = null

  override fun createClassName(cfg: SubGeneratorConfig, table: Definition): String {
    val name = cfg.className
    return name.prefix + nameConverter.convert(table) + name.postfix
  }

  override fun createFieldName(cfg: SubGeneratorConfig, column: Definition): String {
    return nameConverter.convert(column)
  }

  override fun createGetterName(cfg: SubGeneratorConfig, column: Definition): String {
    val name = cfg.accessors.getters

    val result = name.prefix + nameConverter.convert(column) + name.postfix
    return StringUtils.uncapitalize(result)
  }

  override fun createSetterName(cfg: SubGeneratorConfig, column: Definition): String {
    val name = cfg.accessors.setters

    val result = name.prefix + nameConverter.convert(column) + name.postfix
    return StringUtils.uncapitalize(result)
  }

  override fun configure(configuration: Configuration) {
    this.configuration = configuration
  }

  companion object {
    private val DEFAULT = defaultCodeGenerationConfig()

    private fun defaultCodeGenerationConfig(): SubGeneratorConfig {
      return SubGeneratorConfig()
    }
  }
}
