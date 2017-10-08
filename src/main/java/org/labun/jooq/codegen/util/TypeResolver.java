package org.labun.jooq.codegen.util;

import org.jooq.util.ColumnDefinition;
import org.labun.jooq.codegen.task.TaskContext;

/**
 * @author Konstantin Labun
 */
public interface TypeResolver {
    String resolve(TaskContext context, ColumnDefinition column);
}
