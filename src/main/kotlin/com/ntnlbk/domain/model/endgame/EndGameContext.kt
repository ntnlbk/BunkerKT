package com.ntnlbk.domain.model.endgame

import com.ntnlbk.domain.model.Player

data class EndGameContext(
    val players: List<Player>,
    val catastropheDescription: String,
    val bunkerDescription: String,
    val bunkerInventory: String,
    val generateArts: Boolean = false
)
