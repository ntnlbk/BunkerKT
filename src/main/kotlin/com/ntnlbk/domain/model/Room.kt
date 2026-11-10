package com.ntnlbk.domain.model

data class Room(
    val code: String,
    val hostNickname: String,
    val users: List<User>,
    val roomState: RoomState
)
