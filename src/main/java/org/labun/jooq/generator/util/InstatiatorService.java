package org.labun.jooq.generator.util;

import lombok.SneakyThrows;
import org.labun.jooq.generator.config.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Konstantin Labun
 */
public class InstatiatorService {

    private Map<String, Object> instances;
    private Configuration configuration;

    public InstatiatorService(Configuration configuration) {
        this.configuration = configuration;
        this.instances = new HashMap<>();
    }

    @SneakyThrows
    public <T> T createInstance(String className) {
        T instance = (T) Class.forName(className).newInstance();
        if (Configurable.class.isAssignableFrom(instance.getClass())) {
            Configurable configurable = (Configurable) instance;
            configurable.configure(configuration);
        }
        return instance;
    }

    public <T> T createOrGetInstance(String className) {
        return (T) instances.computeIfAbsent(className, this::createInstance);
    }
}
