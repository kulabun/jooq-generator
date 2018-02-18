package org.labun.jooq.generator;

import lombok.Getter;
import lombok.Setter;
import org.labun.jooq.generator.config.Configuration;
import org.labun.jooq.generator.task.TaskContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Konstantin Labun
 */
@Getter
@Setter
public class GeneratorContext {
    private Configuration config;
    private List<TaskContext> taskContexts = new ArrayList<>();

    public Optional<TaskContext> findTaskContextByGeneratorName(String name) {
        return taskContexts.stream()
                .filter(it -> name.equals(it.config().generatorName()))
                .findFirst();
    }

    public TaskContext getTaskContextByGeneratorName(String name) {
        return findTaskContextByGeneratorName(name).orElseThrow(() -> new IllegalStateException(
                String.format("Configuration for %s generator was not found", name)));
    }
}
