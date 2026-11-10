package com.ntnlbk.domain.usecase
import com.ntnlbk.domain.repository.RoomRepository

class CreateRoomUseCase (private val repository: RoomRepository) {
    operator fun invoke(code: String, hostNickname: String) =repository.createRoom(code, hostNickname)
}