package org.labun.jooq.generator.task.domain

import org.jooq.util.CatalogDefinition

/**
 * @author Konstantin Labun
 */
data class Catalog(
    override val definition: CatalogDefinition,
    val name: String,
    val schemas: List<Schema>,

    val simpleCatalogClassName: String,
    val canonicalCatalogClassName: String

    ) : Model<CatalogDefinition>
