package com.ntnlbk.presentation

import com.ntnlbk.di.dbModule
import com.ntnlbk.di.playerModule
import com.ntnlbk.domain.usecase.CreatePlayerUseCase
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
            modules(playerModule, dbModule)
        }
        configureRouting()
    }.start(wait = true)
}

fun Application.configureRouting() {
    routing {
        staticResources("/", "static") {
            default("index.html")
            println("Hello World!")
            val createPlayerUseCase by inject<CreatePlayerUseCase>()
            repeat(12){
                val player = createPlayerUseCase("test")
                println("Created player:\n" +
                        " ${player.nickname}\n" +
                        " ${player.sex}\n" +
                        " ${player.biology}\n" +
                        " ${player.age}\n" +
                        " ${player.profession}\n" +
                        " ${player.workExperience}\n" +
                        " ${player.health}\n" +
                        " ${player.healthScore}\n" +
                        " ${player.hobby}\n" +
                        " ${player.phobia}\n" +
                        " ${player.baggage}\n" +
                        " ${player.factOne}\n" +
                        " ${player.factTwo}\n" +
                        " ${player.specialCards}\n" +
                        " ${player.votesFromPlayers}\n" +
                        " ${player.votesAgainst}\n" +
                        " ${player.isAlive}\n")
            }

        }
    }
}