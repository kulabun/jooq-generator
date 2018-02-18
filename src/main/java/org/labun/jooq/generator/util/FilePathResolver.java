package org.labun.jooq.generator.util;

import org.labun.jooq.generator.config.CodeGenerationConfig;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Konstantin Labun
 */
public class FilePathResolver {

    private CodeGenerationConfig config;

    public FilePathResolver(CodeGenerationConfig config) {
        this.config = config;
    }

    public Path resolveFilePath(String className) {
        return Paths.get(config.generatedSourcesRoot(), fileName(className));
    }

    private String fileName(String className) {
        return packagePath() + File.separator + className + ".java";
    }

    private String packagePath() {
        return packageName().replace(".", File.separator);
    }

    private String packageName() {
        return config.packageName();
    }
}
