package com.ntnlbk.presentation.routing

import com.ntnlbk.domain.model.RoomState
import com.ntnlbk.domain.usecase.GetRoomUseCase
import com.ntnlbk.presentation.dto.mapper.GameDTOMapper
import com.ntnlbk.presentation.dto.websocket.ClientMessage
import com.ntnlbk.presentation.dto.websocket.ServerMessage
import com.ntnlbk.presentation.dto.websocket.WebSocketManager
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json

fun Route.gameGeneralRouting(
    wsManager: WebSocketManager,
    gameDTOMapper: GameDTOMapper,
    getRoomUseCase: GetRoomUseCase
) {

    val webSocketJson = Json {
        classDiscriminator = "type"
        ignoreUnknownKeys = true
        isLenient = true
    }

    webSocket("/ws/game/{code}/{nickname}") {
        val roomCode = call.parameters["code"] ?: throw IllegalArgumentException("Missing room code")
        val nickname = call.parameters["nickname"] ?: throw IllegalArgumentException("Missing username")
        wsManager.addSession(roomCode, this)

        try {
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val rawText = frame.readText()

                    val clientMessage = try {
                        webSocketJson.decodeFromString<ClientMessage>(rawText)
                    } catch (e: Exception) {
                        continue
                    }

                    when (clientMessage) {
                        is ClientMessage.ReadyForGameStart -> {
                            val room = getRoomUseCase(roomCode)
                                ?: throw IllegalArgumentException("Unknown room code $roomCode")
                            val gameState =
                                room.roomState as? RoomState.Game
                                    ?: throw IllegalArgumentException(
                                        "Game has not started yet"
                                    )

                            val response: ServerMessage = ServerMessage.GameState(
                                gameDTOMapper.mapDomainGameStateToGameStateDTO(gameState)
                            )
                            wsManager.broadcast(roomCode, response)
//                            webSocketJson.encodeToString(response)
                        }

                        else -> {
                            // ignore
                        }
                    }
                }
            }
        } finally {
            wsManager.removeSession(roomCode, this)
        }

    }
}