package com.ntnlbk.presentation.dto.websocket

import com.ntnlbk.domain.model.PlayerTraitName
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

    @Serializable
    @SerialName("UPDATE_PLAYER_IS_ALIVE")
    data class UpdatePlayerIsAlive(
        val nickname: String,
        val isAlive: Boolean,
    ): ClientMessage

    @Serializable
    @SerialName("UPDATE_PLAYER_TRAIT_VISIBILITY")
    data class UpdatePlayerTrait (
        val nickname: String,
        val traitName: PlayerTraitName,
        val isOpen: Boolean,
    ): ClientMessage
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