package org.labun.jooq.generator.util

import org.labun.jooq.generator.config.Configuration

/**
 * @author Konstantin Labun
 */
class InstantiationService(
    private val configuration: Configuration
) {

  private val instances: MutableMap<String, Any> = HashMap()

  private fun <T> createInstance(className: String): T {
    val instance = Class.forName(className).newInstance() as T
    if (instance is Configurable) instance.configure(configuration)
    return instance
  }

  fun <T> createOrGetInstance(className: String): T {
    return instances.computeIfAbsent(className, { this.createInstance(it) }) as T
  }
}
