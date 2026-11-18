package com.ntnlbk.presentation.dto.model

import kotlinx.serialization.Serializable

@Serializable
data class PlayerDTO(
    val nickname: String,
    val sex: PlayerTraitDTO,
    val biology: PlayerTraitDTO,
    val age: PlayerTraitDTO,
    val profession: PlayerTraitDTO,
    val workExperience: PlayerTraitDTO,
    val health: PlayerTraitDTO,
    val healthScore: PlayerTraitDTO,
    val hobby: PlayerTraitDTO,
    val phobia: PlayerTraitDTO,
    val baggage: PlayerTraitDTO,
    val factOne: PlayerTraitDTO,
    val factTwo: PlayerTraitDTO,
    val specialCards: List<String>,
    val votesFromPlayers: List<Int> = listOf(),
    val votesAgainst: Int?,
    val isAlive: Boolean
)
