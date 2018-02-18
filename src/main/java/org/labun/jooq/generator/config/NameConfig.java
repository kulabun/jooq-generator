package org.labun.jooq.generator.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Konstantin Labun
 */
@Getter
@Setter
public class NameConfig {
    private String prefix = "";
    private String postfix = "";
    private String name = "";
}
