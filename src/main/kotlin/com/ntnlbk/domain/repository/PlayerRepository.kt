package com.ntnlbk.domain.repository

import com.ntnlbk.domain.model.Player
import com.ntnlbk.domain.model.PlayerTrait
import com.ntnlbk.domain.model.PlayerTraitName

interface PlayerRepository {
    fun createPlayer(nickname: String): Player
    fun getRandomTrait(traitName: PlayerTraitName): PlayerTrait
}