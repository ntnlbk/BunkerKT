package com.ntnlbk.domain.model

sealed class RoomState {

    object Lobby: RoomState()
    data class Game(
        val players: List<Player>,
        val gameStage: GameStage,

    ): RoomState()
}