package com.ntnlbk.domain.usecase.player

import com.ntnlbk.domain.model.PlayerTraitName
import com.ntnlbk.domain.repository.PlayerRepository

class GetRandomTraitUseCase (private val repository: PlayerRepository) {
    operator fun invoke(traitName: PlayerTraitName) = repository.getRandomTrait(traitName)
}