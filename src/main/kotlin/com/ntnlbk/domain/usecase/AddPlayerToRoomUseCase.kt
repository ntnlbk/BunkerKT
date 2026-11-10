package com.ntnlbk.domain.usecase

import com.ntnlbk.domain.repository.RoomRepository

class AddPlayerToRoomUseCase (private val repository: RoomRepository) {
    operator fun invoke(code: String, playerNickname: String) = repository.addPlayerToRoom(code, playerNickname)
}