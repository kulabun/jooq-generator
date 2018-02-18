package org.labun.jooq.generator.util;

import org.apache.commons.lang3.StringUtils;
import org.jooq.util.Definition;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Konstantin Labun
 */
public class TableNameConverter {

    public String convert(Definition element) {
        String name = element.getName();
        String[] words = name.split("_");

        return Arrays.stream(words)
                .map(String::toLowerCase)
                .map(StringUtils::capitalize)
                .collect(Collectors.joining(""));
    }
}
