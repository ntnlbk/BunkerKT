package com.ntnlbk.data.mapper

import com.ntnlbk.data.dbmodel.PlayerTraitDBModel
import com.ntnlbk.domain.model.PlayerTrait

class PlayerTraitMapper {
    fun mapDbModelToDomainModel(playerTraitDbMo: PlayerTraitDBModel): PlayerTrait {
        return PlayerTrait(
            value = playerTraitDbMo.value, isOpen = false
        )
    }
}