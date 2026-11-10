package com.ntnlbk.data.postgresql.table

import org.jetbrains.exposed.sql.Table

object ProfessionTable : Table("profession") {
    val id = integer("id")
    val value = text("value")
}