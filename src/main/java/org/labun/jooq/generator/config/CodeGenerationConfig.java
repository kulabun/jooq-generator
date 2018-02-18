package org.labun.jooq.generator.config;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.labun.jooq.generator.util.DefaultJavaTypeResolver;
import org.labun.jooq.generator.util.DefaultNameCreator;
import org.labun.jooq.generator.util.DefaultSqlTypeResolver;

/**
 * @author Konstantin Labun
 */
@Getter
@Setter
public class CodeGenerationConfig {
    @NonNull private String generatorName;
    @NonNull private String generatedSourcesRoot;
    @NonNull private String packageName;
    @NonNull private NameConfig className = new NameConfig();
    @NonNull private AccessorsConfig accessors = new AccessorsConfig();
    @NonNull private String nameCreator = DefaultNameCreator.class.getCanonicalName();
    @NonNull private String template;
    private boolean javaTimeDates;
    @NonNull private String sqlTypeResolver = DefaultSqlTypeResolver.class.getCanonicalName();
    @NonNull private String javaTypeResolver = DefaultJavaTypeResolver.class.getCanonicalName();
    @NonNull private String codeGenerationTask;
}
