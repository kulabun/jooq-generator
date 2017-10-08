package org.labun.jooq.codegen;

import org.jooq.util.Database;

/**
 * @author Konstantin Labun
 */
public interface Generator {
    void generate(Database db);
}
