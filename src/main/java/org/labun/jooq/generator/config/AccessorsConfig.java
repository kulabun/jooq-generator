package org.labun.jooq.generator.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Konstantin Labun
 */
@Getter
@Setter
public class AccessorsConfig {
    private NameConfig getters = new NameConfig();
    private NameConfig setters = new NameConfig();
    private boolean fluent = true;
}
