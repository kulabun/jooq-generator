package org.labun.jooq.generator.config;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Konstantin Labun
 */
@Getter
@Setter
public class Configuration {
    private DatabaseConfig database = new DatabaseConfig();
    private List<CodeGenerationConfig> codeGeneration = new ArrayList<>();

    public Optional<CodeGenerationConfig> findCodeGenerationConfigByName(String name) {
        return codeGeneration.stream()
                .filter(it -> name.equals(it.generatorName()))
                .findFirst();
    }

    public CodeGenerationConfig getCodeGenerationConfigByName(String name) {
        return findCodeGenerationConfigByName(name).orElseThrow(() -> new IllegalStateException(
                String.format("Configuration for %s generator was not found", name)));
    }
}
