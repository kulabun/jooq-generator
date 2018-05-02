package org.labun.jooq.codegen

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.labun.jooq.generator.DefaultGenerator
import org.labun.jooq.generator.Defaults
import org.labun.jooq.generator.GenerationTool
import org.labun.jooq.generator.config.Configuration
import org.labun.jooq.generator.config.DatabaseConfig
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors


/**
 * @author Konstantin Labun
 */
class GenerateTableTest {

  @Test
  fun shouldGenerateBaseStructure() {
    val config = createConfig()
    config.database = DatabaseConfig(
        username = "sa",
        password = "",
        driverClass = "org.h2.Driver",
        dbMetaClass = "org.jooq.util.h2.H2Database",
        jdbcUrl = createTestJdbcUrl(),
        schemas = listOf(SCHEMA_NAME)
    )

    GenerationTool.generate(DefaultGenerator(config))
    validateGeneratedClasses(config)
  }

  private fun validateGeneratedClasses(config: Configuration) {
    val expectedPath = this.javaClass.classLoader.getResource("expected")
    val pathes = findClases(Paths.get(expectedPath.toURI()))
    for (path in pathes) validateGeneratedClass(path, config)
  }

  private fun validateGeneratedClass(path: Path, config: Configuration) {
    val expected = Files.readAllBytes(path)

    val relativePath = Paths.get(path.toString().substringAfter("expected"))
    val actual = Files.readAllBytes(actualPath(relativePath, config))
    Assertions.assertArrayEquals(expected, actual, "File: " + path.fileName)
  }

  private fun findClases(path: Path): List<Path> {
    return Files.list(path)
        .collect(Collectors.toList())
        .flatMap {
          if (it.toFile().isFile) listOf(it)
          else findClases(it)
        }
  }

  private fun actualPath(path: Path, cfg: Configuration) =
      Paths.get(cfg.subGenerators[0].generatedSourcesRoot, path.toString())

  private fun createConfig(): Configuration {
    val path = getGeneratedSourceRoot()
    return Defaults.defaultConfiguration()
        .apply { subGenerators.forEach { it -> it.generatedSourcesRoot = path.toString() } }
  }

  private fun getGeneratedSourceRoot(): Path? {
    val classLoader = this.javaClass.classLoader
    val path = Paths.get(classLoader.getResource(".").path)
        .resolveSibling("generated-sources")
    if (Files.notExists(path)) {
      Files.createDirectory(path)
    }
    return path
  }

  private fun createTestJdbcUrl(): String {
    val initScript = this.javaClass.classLoader.getResource("init.sql").path
    return "jdbc:h2:mem:test;INIT=RUNSCRIPT FROM '$initScript' "
  }

  companion object {
    val SCHEMA_NAME = "PUBLIC"
  }
}
