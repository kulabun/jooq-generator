package org.labun.jooq.codegen;

import org.jooq.util.Database;
import org.labun.jooq.codegen.config.Configuration;

/**
 * @author Konstantin Labun
 */
public interface Generator {
    void generate(Database db);

    Configuration configuration();
}
