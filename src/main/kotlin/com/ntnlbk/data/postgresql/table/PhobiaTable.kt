package com.ntnlbk.data.postgresql.table

import org.jetbrains.exposed.sql.Table

object PhobiaTable : Table("phobia") {
    val id = integer("id")
    val value = text("value")
}