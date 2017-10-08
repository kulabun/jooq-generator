package org.labun.jooq.codegen.util;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.util.Definition;
import org.labun.jooq.codegen.config.CodeGenerationConfig;
import org.labun.jooq.codegen.config.Configuration;
import org.labun.jooq.codegen.config.NameConfig;

/**
 * @author Konstantin Labun
 */
@Getter
@Setter
public class DefaultNameCreator implements NameCreator, Configurable {
    private static final CodeGenerationConfig DEFAULT = defaultCodeGenerationConfig();

    private TableNameConverter nameConverter = new TableNameConverter();
    private Configuration configuration;

    private static CodeGenerationConfig defaultCodeGenerationConfig() {
        return new CodeGenerationConfig();
    }

    @Override
    public String createClassName(CodeGenerationConfig cfg, Definition table) {
        NameConfig name = cfg.className();
        return name.prefix() + nameConverter.convert(table) + name.postfix();
    }

    @Override
    public String createGetterName(CodeGenerationConfig cfg, Definition column) {
        NameConfig name = cfg.accessors().getters();

        String result = name.prefix() + nameConverter.convert(column) + name.postfix();
        return StringUtils.uncapitalize(result);
    }

    @Override
    public String createSetterName(CodeGenerationConfig cfg, Definition column) {
        NameConfig name = cfg.accessors().setters();

        String result = name.prefix() + nameConverter.convert(column) + name.postfix();
        return StringUtils.uncapitalize(result);
    }

    @Override
    public void configure(Configuration configuration) {
        this.configuration = configuration;
    }
}
