package com.ntnlbk.data.postgresql.table

import org.jetbrains.exposed.sql.Table

object SpecialCardTable : Table("special_card") {
    val id = integer("id")
    val value = text("value")
}