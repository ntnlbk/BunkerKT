package com.ntnlbk.data.postgresql.table

import org.jetbrains.exposed.sql.Table

object CatastropheDescriptionTable : Table("catastrophe_description") {
    val id = integer("id")
    val value = text("value")
}