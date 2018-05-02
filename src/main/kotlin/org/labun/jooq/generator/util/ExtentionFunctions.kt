package org.labun.jooq.generator.util

/**
 * @author Konstantin Labun
 */
inline fun <T, R> T?.mapNotNull(f: (T) -> R): R? = this?.let(f)

inline fun <T : List<*>, R> T?.mapNotNull(f: (T) -> List<R>): List<R> = this?.let(f) ?: emptyList()
inline fun <T : Set<*>, R> T?.mapNotNull(f: (T) -> Set<R>): Set<R> = this?.let(f) ?: emptySet()
inline fun String.orDefault(value: String): String = if (this.isBlank()) value else this
