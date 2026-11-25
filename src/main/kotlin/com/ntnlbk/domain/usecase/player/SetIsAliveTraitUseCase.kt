package com.ntnlbk.domain.usecase.player

import com.ntnlbk.domain.model.RoomState
import com.ntnlbk.domain.repository.PlayerRepository

class SetIsAliveTraitUseCase(private val repository: PlayerRepository) {
    operator fun invoke(game: RoomState.Game, playerNickname: String, isAlive: Boolean) =
        repository.setIsAliveTrait(game, playerNickname, isAlive)
}