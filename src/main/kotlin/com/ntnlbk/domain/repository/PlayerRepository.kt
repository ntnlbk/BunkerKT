package com.ntnlbk.domain.repository

import com.ntnlbk.domain.model.Player
import com.ntnlbk.domain.model.PlayerTrait
import com.ntnlbk.domain.model.PlayerTraitName
import com.ntnlbk.domain.model.RoomState

interface PlayerRepository {
    fun createPlayer(nickname: String): Player
    fun getRandomTrait(traitName: PlayerTraitName): PlayerTrait
    fun setIsAliveTrait(game: RoomState.Game, playerNickname: String, isAlive: Boolean): RoomState.Game
    fun setTraitIsOpen(
        game: RoomState.Game,
        playerNickname: String,
        trait: PlayerTraitName,
        isOpen: Boolean
    ): RoomState.Game

    fun voteAgainst(
        game: RoomState.Game,
        playerNickname: String,
        targetPlayerNickname: String
    ): RoomState.Game

    fun setRandomTraitForPlayer(game: RoomState.Game, nickname: String, trait: PlayerTraitName): RoomState.Game

    fun setStringAsTraitForPlayer(
        game: RoomState.Game,
        playerNickname: String,
        trait: PlayerTraitName,
        traitValue: String,
    ): RoomState.Game
}