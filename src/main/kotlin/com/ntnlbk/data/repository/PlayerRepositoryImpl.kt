package com.ntnlbk.data.repository

import com.ntnlbk.domain.model.Player
import com.ntnlbk.domain.model.PlayerTrait
import com.ntnlbk.domain.model.PlayerTraitName
import com.ntnlbk.domain.repository.PlayerRepository

class PlayerRepositoryImpl: PlayerRepository {
    override fun createPlayer(nickname: String): Player {
        TODO("Not yet implemented")
    }

    override fun getRandomTrait(traitName: PlayerTraitName): PlayerTrait {
        TODO("Not yet implemented")
    }
}