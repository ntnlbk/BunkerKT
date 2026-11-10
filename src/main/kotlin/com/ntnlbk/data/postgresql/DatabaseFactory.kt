package com.ntnlbk.data.postgresql

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

class DatabaseFactory {
    fun init(): Database {
        val host = System.getenv("DB_HOST") ?: "localhost"
        val port = System.getenv("DB_PORT") ?: "5432"
        val dbName = System.getenv("DB_NAME") ?: "bunker_db"
        val user = System.getenv("DB_USER") ?: "root"
        val password = System.getenv("DB_PASSWORD") ?: "root"

        val config = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = "jdbc:postgresql://$host:$port/$dbName"
            username = user
            setPassword(password)
            maximumPoolSize = 5
            isAutoCommit = false
            validate()
        }

        return Database.connect(HikariDataSource(config))

    }
}