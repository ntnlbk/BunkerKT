package com.ntnlbk.domain.usecase.player

import com.ntnlbk.domain.model.PlayerTraitName
import com.ntnlbk.domain.model.RoomState
import com.ntnlbk.domain.repository.PlayerRepository

class SetStringAsTraitForPlayerUseCase(private val repository: PlayerRepository) {
    operator fun invoke(game: RoomState.Game, nickname: String, traitName: PlayerTraitName, traitValue: String) =
        repository.setStringAsTraitForPlayer(game, nickname, traitName, traitValue)

}