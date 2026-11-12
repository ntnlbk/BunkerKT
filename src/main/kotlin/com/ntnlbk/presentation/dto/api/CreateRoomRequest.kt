package com.ntnlbk.presentation.dto.api

import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomRequest(
    val code: String,
    val hostNickname: String
) {
}