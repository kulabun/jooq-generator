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
    private String username;
    private String password;
    private String driverClass;
    private String dbMetaClass;
    private String jdbcUrl;
    private List<String> schemas = new ArrayList<>();
}
