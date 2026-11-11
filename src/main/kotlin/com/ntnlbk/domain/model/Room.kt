package com.ntnlbk.domain.model

data class Room(
    val code: String,
    val hostNickname: String,
    val users: MutableList<User>,
    val roomState: RoomState
)
