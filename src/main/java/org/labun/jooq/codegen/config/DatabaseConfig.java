package org.labun.jooq.codegen.config;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Konstantin Labun
 */
@Getter
@Setter
public class DatabaseConfig {
    private List<String> schemas = new ArrayList<>();
}
