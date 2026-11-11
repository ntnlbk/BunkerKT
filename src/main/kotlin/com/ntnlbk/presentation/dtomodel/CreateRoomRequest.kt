package com.ntnlbk.presentation.dtomodel

import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomRequest(
    val code: String,
    val hostNickname: String
) {
}