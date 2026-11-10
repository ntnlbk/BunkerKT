package com.ntnlbk.data.postgresql.table

import org.jetbrains.exposed.sql.Table

object HobbyTable : Table("hobby") {
    val id = integer("id")
    val value = text("value")
}