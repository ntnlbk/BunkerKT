package com.ntnlbk.presentation.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            classDiscriminator = "type"
            isLenient = true
            ignoreUnknownKeys = true // Игнорировать лишние поля из JSON-запроса, которых нет в DTO
        })
    }
}