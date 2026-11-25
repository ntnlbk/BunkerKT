package com.ntnlbk.domain.usecase.player

import com.ntnlbk.domain.model.PlayerTraitName
import com.ntnlbk.domain.model.RoomState
import com.ntnlbk.domain.repository.PlayerRepository

class SetTraitIsOpenUseCase(private val repository: PlayerRepository) {
    operator fun invoke(game: RoomState.Game, nickName: String, trait: PlayerTraitName, isOpen: Boolean) =
        repository.setTraitIsOpen(game, nickName, trait, isOpen)
}