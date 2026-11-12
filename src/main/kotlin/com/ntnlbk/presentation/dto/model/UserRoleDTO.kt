package com.ntnlbk.presentation.dto.model

import kotlinx.serialization.Serializable

@Serializable
enum class UserRoleDTO {
    GAME_MASTER, PLAYER, SPECTATOR

}