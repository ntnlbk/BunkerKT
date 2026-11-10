package com.ntnlbk.data.postgresql.table

import org.jetbrains.exposed.sql.Table

object BaggageTable : Table("baggage") {
    val id = integer("id")
    val value = text("value")
}