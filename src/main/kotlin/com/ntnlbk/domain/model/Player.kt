package com.ntnlbk.domain.model

data class Player(
    val nickname: String,
    val sex: PlayerTrait,
    val biology: PlayerTrait,
    val age: PlayerTrait,
    val profession: PlayerTrait,
    val workExperience: PlayerTrait,
    val health: PlayerTrait,
    val healthScore: PlayerTrait,
    val hobby: PlayerTrait,
    val phobia: PlayerTrait,
    val baggage: PlayerTrait,
    val factOne: PlayerTrait,
    val factTwo: PlayerTrait,
    val specialCards: List<String>,
    val votesFromPlayers: List<Int> = listOf(),
    val votesAgainst: Int = -1,
    val isAlive: Boolean = true
)
