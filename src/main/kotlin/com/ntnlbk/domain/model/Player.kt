package com.ntnlbk.domain.model

data class Player(
    val nickname: String,
    val sex: PlayerTrait,
    val biology: PlayerTrait,
    val age: PlayerTrait,
    val profession: PlayerTrait,
    val workExperience: Int,
    val health: PlayerTrait,
    val healthScore: HealthScore,
    val hobby: PlayerTrait,
    val phobia: PlayerTrait,
    val baggage: PlayerTrait,
    val factOne: PlayerTrait,
    val factTwo: PlayerTrait,
    val specialCards: List<String>,
    val votesFromPlayers: List<Int>,
    val votesAgainst: Int = -1,
    val isAlive: Boolean
)
