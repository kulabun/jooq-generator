package org.labun.jooq.codegen;

import junit.framework.Assert;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.jooq.util.Database;
import org.jooq.util.h2.H2Database;
import org.jooq.util.jaxb.Catalog;
import org.jooq.util.jaxb.Schema;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.labun.jooq.codegen.config.CodeGenerationConfig;
import org.labun.jooq.codegen.config.Configuration;
import org.labun.jooq.codegen.config.Defaults;
import org.labun.jooq.codegen.task.SchemaCodeGenerationTask;
import org.labun.jooq.codegen.task.TableCodeGenerationTask;
import org.labun.jooq.codegen.util.FilePathResolver;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;


/**
 * @author Konstantin Labun
 */
public class GenerateTableTest {

    public static final String SCHEMA_NAME = "PUBLIC";
    public static final String TABLE_NAME = "PERSON";

    @Test
    public void shouldGenerateBaseStructure() throws Exception {
        try (Connection connection = initConnection()) {
            assertConnectionSuccess(connection);

            Configuration config = createConfig();
            config.database().schemas(Arrays.asList(SCHEMA_NAME));

            Database db = defaultH2Database();
            db.setConnection(connection);

            new JavaGenerator(config).generate(db);

            validateGeneratedClasses(config);
        }
    }

    private void validateGeneratedClasses(Configuration config) throws IOException, URISyntaxException {
        for (CodeGenerationConfig cfg : config.codeGeneration()) {
            validateGeneratedClass(cfg);
        }
    }

    private void validateGeneratedClass(CodeGenerationConfig cfg) throws IOException, URISyntaxException {
        String className = getClassName(cfg);
        byte[] expected = Files.readAllBytes(expectedFilePath(className));
        byte[] actual = Files.readAllBytes(actualPath(cfg, className));
        Assertions.assertArrayEquals(expected, actual, "Generator: " + cfg.generatorName());
    }

    private String getClassName(CodeGenerationConfig cfg) {
        if (TableCodeGenerationTask.class.getCanonicalName().equals(cfg.codeGenerationTask())) {
            return cfg.className().prefix() + toCamelCase(TABLE_NAME) + cfg.className().postfix();
        } else if (SchemaCodeGenerationTask.class.getCanonicalName().equals(cfg.codeGenerationTask())) {
            return toCamelCase(SCHEMA_NAME);
        }

        Assert.fail("Unsupported CodeGenerationTask: " + cfg.codeGenerationTask());
        return null;
    }

    private Path actualPath(CodeGenerationConfig cfg, String className) {
        return new FilePathResolver(cfg).resolveFilePath(className);
    }

    private Path expectedFilePath(String className) throws URISyntaxException {
        String fileName = "expected/" + className + ".java";
        URL resource = this.getClass().getClassLoader()
                .getResource(fileName);
        if (resource == null) Assert.fail("File not exists: " + fileName);
        return Paths.get(resource.toURI());
    }

    @SneakyThrows
    private Configuration createConfig() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        Path path = Paths.get(classLoader.getResource(".").getPath()).resolveSibling("generated-sources");
        if (Files.notExists(path)) Files.createDirectory(path);

        Configuration configuration = Defaults.configuration();
        configuration.codeGeneration()
                .forEach(it -> it.generatedSourcesRoot(path.toString()));

        return configuration;
    }

    private Database defaultH2Database() {
        Database db = new H2Database();
        db.setConfiguredSchemata(Arrays.asList(new Schema().withInputSchema("")));
        db.setConfiguredCatalogs(Arrays.asList(new Catalog().withInputCatalog("")));
        db.setIncludes(new String[]{".*"});
        db.setConfiguredCustomTypes(Arrays.asList());
        db.setConfiguredEnumTypes(Arrays.asList());
        db.setConfiguredForcedTypes(Arrays.asList());
        return db;
    }

    private void assertConnectionSuccess(Connection connection) throws SQLException {
        connection.createStatement().execute("SELECT 1");
    }

    @SneakyThrows
    private Connection initConnection() {
        Class.forName("org.h2.Driver");

        String initScript = this.getClass().getClassLoader().getResource("init.sql").getPath();
        return DriverManager.getConnection("jdbc:h2:mem:test;INIT=RUNSCRIPT FROM '" + initScript + "' ", "sa", "");
    }

    public String toCamelCase(String str) {
        return Arrays.stream(str.toLowerCase().split("_"))
                .map(word -> StringUtils.capitalize(word))
                .collect(Collectors.joining(""));
    }
}
