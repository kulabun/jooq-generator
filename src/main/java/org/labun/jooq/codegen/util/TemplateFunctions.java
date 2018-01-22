package org.labun.jooq.codegen.util;

import org.labun.jooq.codegen.task.TableCodeGenerationTask;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Konstantin Labun
 */
public class TemplateFunctions {

    private static final List<String> SEARCHABLE_DATA_TYPES = Arrays.asList(
            "org.jooq.impl.SQLDataType.VARCHAR",
            "org.jooq.impl.SQLDataType.CHAR",
            "org.jooq.impl.SQLDataType.LONGVARCHAR",
            "org.jooq.impl.SQLDataType.NVARCHAR",
            "org.jooq.impl.SQLDataType.NCHAR",
            "org.jooq.impl.SQLDataType.LONGNVARCHAR",
            "org.jooq.impl.SQLDataType.BOOLEAN",
            "org.jooq.impl.SQLDataType.BIT",
            "org.jooq.impl.SQLDataType.TINYINT",
            "org.jooq.impl.SQLDataType.SMALLINT",
            "org.jooq.impl.SQLDataType.INTEGER",
            "org.jooq.impl.SQLDataType.BIGINT",
            "org.jooq.impl.SQLDataType.DECIMAL_INTEGER",
            "org.jooq.impl.SQLDataType.TINYINTUNSIGNED",
            "org.jooq.impl.SQLDataType.SMALLINTUNSIGNED",
            "org.jooq.impl.SQLDataType.INTEGERUNSIGNED",
            "org.jooq.impl.SQLDataType.BIGINTUNSIGNED",
            "org.jooq.impl.SQLDataType.DOUBLE",
            "org.jooq.impl.SQLDataType.FLOAT",
            "org.jooq.impl.SQLDataType.REAL",
            "org.jooq.impl.SQLDataType.NUMERIC",
            "org.jooq.impl.SQLDataType.DECIMAL",
            "org.jooq.impl.SQLDataType.DATE",
            "org.jooq.impl.SQLDataType.TIMESTAMP",
            "org.jooq.impl.SQLDataType.TIME",
            "org.jooq.impl.SQLDataType.INTERVALYEARTOMONTH",
            "org.jooq.impl.SQLDataType.INTERVALDAYTOSECOND",
            "org.jooq.impl.SQLDataType.LOCALDATE",
            "org.jooq.impl.SQLDataType.LOCALTIME",
            "org.jooq.impl.SQLDataType.LOCALDATETIME",
            "org.jooq.impl.SQLDataType.OFFSETTIME",
            "org.jooq.impl.SQLDataType.OFFSETDATETIME",
            "org.jooq.impl.SQLDataType.TIMEWITHTIMEZONE",
            "org.jooq.impl.SQLDataType.TIMESTAMPWITHTIMEZONE",
            "org.jooq.impl.SQLDataType.UUID"
    );

    private static final List<String> COMPARABLE_DATA_TYPES = Arrays.asList(
            "org.jooq.impl.SQLDataType.TINYINT",
            "org.jooq.impl.SQLDataType.SMALLINT",
            "org.jooq.impl.SQLDataType.INTEGER",
            "org.jooq.impl.SQLDataType.BIGINT",
            "org.jooq.impl.SQLDataType.DECIMAL_INTEGER",
            "org.jooq.impl.SQLDataType.TINYINTUNSIGNED",
            "org.jooq.impl.SQLDataType.SMALLINTUNSIGNED",
            "org.jooq.impl.SQLDataType.INTEGERUNSIGNED",
            "org.jooq.impl.SQLDataType.BIGINTUNSIGNED",
            "org.jooq.impl.SQLDataType.DOUBLE",
            "org.jooq.impl.SQLDataType.FLOAT",
            "org.jooq.impl.SQLDataType.REAL",
            "org.jooq.impl.SQLDataType.NUMERIC",
            "org.jooq.impl.SQLDataType.DECIMAL",
            "org.jooq.impl.SQLDataType.DATE",
            "org.jooq.impl.SQLDataType.TIMESTAMP",
            "org.jooq.impl.SQLDataType.TIME",
            "org.jooq.impl.SQLDataType.INTERVALYEARTOMONTH",
            "org.jooq.impl.SQLDataType.INTERVALDAYTOSECOND",
            "org.jooq.impl.SQLDataType.LOCALDATE",
            "org.jooq.impl.SQLDataType.LOCALTIME",
            "org.jooq.impl.SQLDataType.LOCALDATETIME",
            "org.jooq.impl.SQLDataType.OFFSETTIME",
            "org.jooq.impl.SQLDataType.OFFSETDATETIME",
            "org.jooq.impl.SQLDataType.TIMEWITHTIMEZONE",
            "org.jooq.impl.SQLDataType.TIMESTAMPWITHTIMEZONE"
    );


    private TemplateFunctions() {
    }

    public static String toCamelCase(String src) {
        return Stream.of(src.split("_"))
                .map(it -> it.toLowerCase())
                .map(it -> capitalize(it))
                .collect(Collectors.joining(""));
    }

    public static String capitalize(String src) {
        if (src.length() == 0) return src;
        return Character.toUpperCase(src.charAt(0)) + src.substring(1);
    }

    public static String decapitalize(String src) {
        if (src.length() == 0) return src;
        return Character.toLowerCase(src.charAt(0)) + src.substring(1);
    }

    public static boolean isUniqKey(List<TableCodeGenerationTask.UniqKey> keys,
                                    TableCodeGenerationTask.Column column) {
        for (TableCodeGenerationTask.UniqKey key : keys) {
            if (key.columns().contains(column)
                    && key.columns().size() == 1)
                return true;
        }
        return false;
    }

    public static boolean isUniqKey(List<TableCodeGenerationTask.UniqKey> keys,
                                    List<TableCodeGenerationTask.Column> columns) {
        for (TableCodeGenerationTask.UniqKey key : keys) {
            if (key.columns().containsAll(columns)
                    && key.columns().size() == columns.size())
                return true;
        }
        return false;
    }

    public static boolean isSearchable(TableCodeGenerationTask.Column column) {
        return SEARCHABLE_DATA_TYPES.stream()
                .anyMatch(it -> column.getSqlType().equals(it)
                        || column.getSqlType().startsWith(it + "."));
    }

    public static boolean isComparable(TableCodeGenerationTask.Column column) {
        return COMPARABLE_DATA_TYPES.stream()
                .anyMatch(it -> column.getSqlType().equals(it)
                        || column.getSqlType().startsWith(it + "."));
    }

    public static Object conditional(boolean condition, Object r1, Object r2) {
        return condition ? r1 : r2;
    }
}
