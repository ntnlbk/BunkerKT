package com.ntnlbk.presentation.dto.websocket

import com.ntnlbk.presentation.dto.model.GameStateDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface ClientMessage {
    @Serializable
    @SerialName("START_GAME")
    data object StartGame : ClientMessage
    @Serializable
    @SerialName("READY_FOR_GAME_START")
    data object ReadyForGameStart: ClientMessage
}

@Serializable
sealed interface ServerMessage {
    @Serializable
    @SerialName("GAME_STARTED")
    data object GameStarted: ServerMessage
    @Serializable
    @SerialName("GAME_STATE")
    data class GameState(
        val gameState: GameStateDTO
    ): ServerMessage



}