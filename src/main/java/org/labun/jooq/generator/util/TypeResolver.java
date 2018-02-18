package org.labun.jooq.generator.util;

import org.jooq.util.ColumnDefinition;
import org.labun.jooq.generator.task.TaskContext;

/**
 * @author Konstantin Labun
 */
public interface TypeResolver {
    String resolve(TaskContext context, ColumnDefinition column);
}
