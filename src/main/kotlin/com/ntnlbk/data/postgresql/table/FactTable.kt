package com.ntnlbk.data.postgresql.table

import org.jetbrains.exposed.sql.Table

object FactTable : Table("fact") {
    val id = integer("id")
    val value = text("value")
}