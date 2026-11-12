package com.ntnlbk.presentation.dto.api

import com.ntnlbk.presentation.dto.model.UserDTO
import kotlinx.serialization.Serializable

@Serializable
data class JoinRoomRequest(
    val code: String,
    val user: UserDTO,
)
