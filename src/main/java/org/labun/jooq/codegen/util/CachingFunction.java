package org.labun.jooq.codegen.util;

import java.util.function.Function;

/**
 * @author Konstantin Labun
 */
public class CachingFunction<T, R> implements Function<T, R> {
    private Function<T, R> delegate;
    private R result;

    public CachingFunction(Function<T, R> delegate) {
        this.delegate = delegate;
    }

    @Override
    public R apply(T o) {
        if (result == null) {
            result = delegate.apply(o);
        }
        return result;
    }
}
