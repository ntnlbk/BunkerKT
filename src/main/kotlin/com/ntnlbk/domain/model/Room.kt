package com.ntnlbk.domain.model

data class Room(
    val code: String,
    val hostId: String,
    val players: List<Player>,
    val gameStatus: GameStatus
)
