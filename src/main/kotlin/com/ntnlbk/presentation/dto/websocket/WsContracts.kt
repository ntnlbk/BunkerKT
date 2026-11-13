package com.ntnlbk.presentation.dto.websocket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface ClientMessage {
    @Serializable
    @SerialName("START_GAME")
    data object StartGame : ClientMessage
}

@Serializable
sealed interface ServerMessage {
    @Serializable
    @SerialName("GAME_STARTED")
    data object GameStarted: ServerMessage

}