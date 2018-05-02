package org.labun.jooq.generator.util

import org.labun.jooq.generator.task.domain.Column
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * @author Konstantin Labun
 */
object TemplateFunctions {

  private val SEARCHABLE_DATA_TYPES = Arrays.asList(
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
  )

  private val COMPARABLE_DATA_TYPES = Arrays.asList(
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
  )

  fun toCamelCase(src: String): String {
    return Stream.of<String>(
        *src.split("_".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray())
        .map<String> { it -> it.toLowerCase() }
        .map { it -> capitalize(it) }
        .collect(Collectors.joining(""))
  }

  fun capitalize(src: String): String {
    return if (src.length == 0) src else Character.toUpperCase(src[0]) + src.substring(1)
  }

  fun decapitalize(src: String): String {
    return if (src.length == 0) src else Character.toLowerCase(src[0]) + src.substring(1)
  }

//  fun isUniqKey(keys: List<UniqKey>,
//      column: Column): Boolean {
//    for (key in keys) {
//      if (key.columns().contains(column) && key.columns().size == 1)
//        return true
//    }
//    return false
//  }

//  fun isUniqKey(keys: List<UniqKey>,
//      columns: List<Column>): Boolean {
//    for (key in keys) {
//      if (key.columns().containsAll(columns) && key.columns().size == columns.size)
//        return true
//    }
//    return false
//  }

  fun isSearchable(column: Column) =
      SEARCHABLE_DATA_TYPES.filter {
        column.sqlType == it || column.sqlType.startsWith("$it.")
      }.any()

  fun isComparable(column: Column) =
      COMPARABLE_DATA_TYPES.filter { it ->
        column.sqlType == it || column.sqlType.startsWith("$it.")
      }.any()

  fun conditional(condition: Boolean, r1: Any, r2: Any): Any {
    return if (condition) r1 else r2
  }
}
