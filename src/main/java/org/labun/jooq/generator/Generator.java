package org.labun.jooq.generator;

import org.jooq.util.Database;
import org.labun.jooq.generator.config.Configuration;

/**
 * @author Konstantin Labun
 */
public interface Generator {
    void generate(Database db);

    Configuration configuration();
}
