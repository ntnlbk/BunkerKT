package com.ntnlbk.presentation

import com.ntnlbk.di.dbModule
import com.ntnlbk.di.endGameModule
import com.ntnlbk.di.gameModule
import com.ntnlbk.di.playerModule
import com.ntnlbk.di.presentationModule
import com.ntnlbk.di.roomModule
import com.ntnlbk.domain.usecase.game.EndGameUseCase
import com.ntnlbk.domain.usecase.room.AddUserToRoomUseCase
import com.ntnlbk.domain.usecase.room.CreateRoomUseCase
import com.ntnlbk.domain.usecase.game.EndVotingUseCase
import com.ntnlbk.domain.usecase.room.GetRoomUseCase
import com.ntnlbk.domain.usecase.player.SetIsAliveTraitUseCase
import com.ntnlbk.domain.usecase.player.SetRandomTraitForPlayerUseCase
import com.ntnlbk.domain.usecase.player.SetStringAsTraitForPlayerUseCase
import com.ntnlbk.domain.usecase.player.SetTraitIsOpenUseCase
import com.ntnlbk.domain.usecase.game.StartGameUseCase
import com.ntnlbk.domain.usecase.game.StartVotingUseCase
import com.ntnlbk.domain.usecase.player.MixTraitForPlayersUseCase
import com.ntnlbk.domain.usecase.player.VoteAgainstUseCase
import com.ntnlbk.plugins.configureWebSockets
import com.ntnlbk.presentation.dto.mapper.GameDTOMapper
import com.ntnlbk.presentation.dto.mapper.UserDTOMapper
import com.ntnlbk.presentation.dto.websocket.WebSocketManager
import com.ntnlbk.presentation.plugins.configureSerialization
import com.ntnlbk.presentation.routing.gameGeneralRouting
import com.ntnlbk.presentation.routing.roomRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

fun main() {
    println("Hello World!")
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(Koin) {
            modules(playerModule, dbModule, roomModule, presentationModule, gameModule, endGameModule)
        }
        configureSerialization()
        configureWebSockets()
        configureRouting()
    }.start(wait = true)
}

fun Application.configureRouting() {
    val createRoomUseCase by inject<CreateRoomUseCase>()
    val getRoomUseCase by inject<GetRoomUseCase>()
    val mapper by inject<UserDTOMapper>()
    val addUserToRoomUseCase by inject<AddUserToRoomUseCase>()
    val webSocketManager by inject<WebSocketManager>()
    val startGameUseCase by inject<StartGameUseCase>()
    val gameDTOMapper by inject<GameDTOMapper>()
    val setIsAliveTraitUseCase by inject<SetIsAliveTraitUseCase>()
    val setTraitIsOpenUseCase by inject<SetTraitIsOpenUseCase>()
    val startVotingUseCase by inject<StartVotingUseCase>()
    val endVotingUseCase by inject<EndVotingUseCase>()
    val voteAgainstUseCase by inject<VoteAgainstUseCase>()
    val setRandomTraitForPlayerUseCase by inject<SetRandomTraitForPlayerUseCase>()
    val setStringAsTraitForPlayerUseCase by inject<SetStringAsTraitForPlayerUseCase>()
    val mixTraitForPlayersUseCase by inject<MixTraitForPlayersUseCase>()
    val endGameUseCase by inject<EndGameUseCase>()

    routing {
        staticResources("/", "static") {
            default("index.html")
            println("Hello World!")
        }
        roomRouting(createRoomUseCase, getRoomUseCase, addUserToRoomUseCase, mapper, webSocketManager, startGameUseCase)
        gameGeneralRouting(
            webSocketManager,
            gameDTOMapper,
            getRoomUseCase,
            setIsAliveTraitUseCase,
            setTraitIsOpenUseCase,
            startVotingUseCase,
            endVotingUseCase,
            voteAgainstUseCase,
            setRandomTraitForPlayerUseCase,
            setStringAsTraitForPlayerUseCase,
            mixTraitForPlayersUseCase,
            endGameUseCase
        )
    }
}