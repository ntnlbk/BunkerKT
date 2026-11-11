package com.ntnlbk.presentation

import com.ntnlbk.di.dbModule
import com.ntnlbk.di.playerModule
import com.ntnlbk.di.roomModule
import com.ntnlbk.domain.usecase.CreateRoomUseCase
import com.ntnlbk.presentation.plugins.configureSerialization
import com.ntnlbk.presentation.routing.roomRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(Koin) {
            modules(playerModule, dbModule, roomModule)
        }
        configureSerialization()
        configureRouting()
    }.start(wait = true)
}

fun Application.configureRouting() {
    routing {
        staticResources("/", "static") {
            default("index.html")
            println("Hello World!")
        }
        val createRoomUseCase by inject<CreateRoomUseCase>()
        roomRouting(createRoomUseCase)
    }
}