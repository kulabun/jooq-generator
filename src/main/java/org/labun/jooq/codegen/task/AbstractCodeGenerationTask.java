package org.labun.jooq.codegen.task;

import lombok.SneakyThrows;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.jooq.util.Definition;
import org.labun.jooq.codegen.config.Defaults;
import org.labun.jooq.codegen.util.TemplateFunctions;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Konstantin Labun
 */
public abstract class AbstractCodeGenerationTask<T extends Definition> implements CodeGenerationTask<T> {

    protected TaskContext ctx;
    private Template template;

    public AbstractCodeGenerationTask(TaskContext ctx) {
        this.ctx = ctx;
    }

    @Override
    @SneakyThrows
    public void generate(T t) {
        VelocityContext context = createContext(t);
        write(context, getFilePath(t));
    }

    private VelocityContext createContext(T t) {
        return configureContext(t, new VelocityContext());
    }

    protected VelocityContext configureContext(T t, VelocityContext context) {
        context.put(Defaults.TemplateVariables.PACKAGE, ctx.config().packageName());
        context.put(Defaults.TemplateVariables.CLASS_NAME, generateClassName(t));
        context.put("util", TemplateFunctions.class);
        return context;
    }

    private Path getFilePath(T t) {
        return createDirs(ctx.filePathResolver().resolveFilePath(generateClassName(t)));
    }

    @SneakyThrows
    private Path createDirs(Path path) {
        if (!Files.exists(path.getParent()))
            Files.createDirectories(path.getParent());
        return path;
    }

    private String generateClassName(T t) {
        return ctx.nameCreator().createClassName(ctx.config(), t);
    }

    private void write(VelocityContext context, Path path) throws IOException {
        Template template = getTemplate(ctx.config().template());
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            template.merge(context, writer);
        }
    }

    private Template getTemplate(String templatePath) {
        if (template == null) {
            template = Velocity.getTemplate(templatePath);
        }
        return template;
    }
}