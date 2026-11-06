package com.ntnlbk.presentation

import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.staticResources
import com.ntnlbk.di.appModule
import com.ntnlbk.domain.DummyUseCase
import io.ktor.server.netty.Netty
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(Koin) {
            modules(appModule)
        }
        configureRouting()
    }.start(wait = true)
}

fun Application.configureRouting() {
    routing {
        staticResources("/", "static") {
            default("index.html")
            println("Hello World!")
            val getBaggageUseCase by inject<DummyUseCase>()
            println(getBaggageUseCase("PW"))
        }
    }
}