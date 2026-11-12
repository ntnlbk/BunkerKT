package com.ntnlbk.presentation.dto.websocket

import com.ntnlbk.presentation.dto.model.GameStateDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface ClientMessage {
    @Serializable
    @SerialName("START_GAME")
    data class StartGame(
        val roomCode: String
    ) : ClientMessage
}

@Serializable
sealed interface ServerMessage {
    @Serializable
    @SerialName("GAME_STARTED")
    data class GameStarted(
        val gameState: GameStateDTO
    ) : ServerMessage

}