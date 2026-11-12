package com.ntnlbk.presentation.dto.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val nickname: String,
    val role: UserRoleDTO,
)
