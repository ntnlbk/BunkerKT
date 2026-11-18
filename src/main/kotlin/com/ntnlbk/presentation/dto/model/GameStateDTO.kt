package com.ntnlbk.presentation.dto.model

import kotlinx.serialization.Serializable

@Serializable
data class GameStateDTO(
    val players: List<PlayerDTO>,
    val catastropheDescription: String,
    val bunkerDescription: String,
    val bunkerInventory: String
)
