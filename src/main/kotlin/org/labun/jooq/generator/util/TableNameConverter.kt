package org.labun.jooq.generator.util

import org.apache.commons.lang3.StringUtils
import org.jooq.util.Definition
import java.util.*
import java.util.stream.Collectors

/**
 * @author Konstantin Labun
 */
class TableNameConverter {

  fun convert(element: Definition): String {
    val name = element.name
    val words = name.split("_".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()

    return Arrays.stream<String>(words)
        .map { it.toLowerCase() }
        .map { StringUtils.capitalize(it) }
        .collect(Collectors.joining(""))
  }
}
