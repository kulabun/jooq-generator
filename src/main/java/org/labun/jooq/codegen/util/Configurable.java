package org.labun.jooq.codegen.util;


import org.labun.jooq.codegen.config.Configuration;

/**
 * @author Konstantin Labun
 */
public interface Configurable {
    void configure(Configuration configuration);
}
