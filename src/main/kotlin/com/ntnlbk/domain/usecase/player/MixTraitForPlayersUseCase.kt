package com.ntnlbk.domain.usecase.player

import com.ntnlbk.domain.model.PlayerTraitName
import com.ntnlbk.domain.model.RoomState
import com.ntnlbk.domain.repository.PlayerRepository

class MixTraitForPlayersUseCase(private val repository: PlayerRepository) {
    operator fun invoke(game: RoomState.Game, traitName: PlayerTraitName, playersNicknames: List<String>) =
        repository.mixTraitForPlayers(game, traitName, playersNicknames)
}