package com.ntnlbk.domain.model

sealed class RoomState {

    object Lobby: RoomState()
    data class Game(
        val code: String,
        val players: List<Player>,
        val gameStage: GameStage,
        val catastropheDescription: String,
        val bunkerDescription: String,
        val bunkerInventory: String
    ): RoomState()
}