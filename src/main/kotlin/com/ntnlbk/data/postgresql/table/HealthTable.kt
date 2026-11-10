package com.ntnlbk.data.postgresql.table

import org.jetbrains.exposed.sql.Table

object HealthTable : Table("health") {
    val id = integer("id")
    val value = text("value")
}