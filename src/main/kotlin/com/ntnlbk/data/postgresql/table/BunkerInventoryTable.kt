package com.ntnlbk.data.postgresql.table

import org.jetbrains.exposed.sql.Table

object BunkerInventoryTable : Table("bunker_inventory") {
    val id = integer("id")
    val value = text("value")
}