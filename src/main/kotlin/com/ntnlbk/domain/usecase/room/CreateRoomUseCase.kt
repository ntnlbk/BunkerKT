package com.ntnlbk.domain.usecase.room
import com.ntnlbk.domain.model.User
import com.ntnlbk.domain.repository.RoomRepository

class CreateRoomUseCase (private val repository: RoomRepository) {
    operator fun invoke(code: String, gm: User) =repository.createRoom(code, gm)
}