package com.ntnlbk.data.postgresql.table

import org.jetbrains.exposed.sql.Table

object BunkerDescriptionTable : Table("bunker_description") {
    val id = integer("id")
    val value = text("value")
}