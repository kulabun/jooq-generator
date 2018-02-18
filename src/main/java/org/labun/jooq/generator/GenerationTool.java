package org.labun.jooq.generator;

import lombok.SneakyThrows;
import org.jooq.util.Database;
import org.jooq.util.jaxb.Catalog;
import org.jooq.util.jaxb.Schema;
import org.labun.jooq.generator.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * @author Konstantin Labun
 */
public class GenerationTool {

    @SneakyThrows
    public static void generate(Generator generator) {
        DatabaseConfig db = generator.configuration().database();
        Class.forName(db.driverClass());

        try (Connection connection = DriverManager.getConnection(
                db.jdbcUrl(),
                db.username(),
                db.password())) {
            assertConnectionSuccess(connection);
            generator.generate(createDbMeta(db, connection));
        }
    }

    private static Database createDbMeta(DatabaseConfig db, Connection connection) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Database dbMeta = (Database) Class.forName(db.dbMetaClass()).newInstance();
        configureDefaults(dbMeta);
        dbMeta.setConnection(connection);
        return dbMeta;
    }

    private static void assertConnectionSuccess(Connection connection) throws SQLException {
        connection.createStatement().execute("SELECT 1");
    }

    private static void configureDefaults(Database db) {
        db.setConfiguredSchemata(Arrays.asList(new Schema().withInputSchema("")));
        db.setConfiguredCatalogs(Arrays.asList(new Catalog().withInputCatalog("")));
        db.setIncludes(new String[]{".*"});
        db.setConfiguredCustomTypes(Arrays.asList());
        db.setConfiguredEnumTypes(Arrays.asList());
        db.setConfiguredForcedTypes(Arrays.asList());
    }

    private GenerationTool() {

    }
}
