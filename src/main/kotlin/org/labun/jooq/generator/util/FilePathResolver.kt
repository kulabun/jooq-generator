package org.labun.jooq.generator.util

import org.labun.jooq.generator.config.SubGeneratorConfig
import java.io.File
import java.nio.file.Paths

/**
 * @author Konstantin Labun
 */
class FilePathResolver(private val taskConfigSub: SubGeneratorConfig) {

  fun resolveFilePath(className: String) =
      Paths.get(taskConfigSub.generatedSourcesRoot, fileName(className))

  private fun fileName(className: String) = "${packagePath()}${File.separator}${className}.java"

  private fun packagePath() = packageName().replace(".", File.separator)

  private fun packageName() = taskConfigSub.packageName
}