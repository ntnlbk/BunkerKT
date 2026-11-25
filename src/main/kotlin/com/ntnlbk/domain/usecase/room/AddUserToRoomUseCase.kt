package com.ntnlbk.domain.usecase.room

import com.ntnlbk.domain.model.User
import com.ntnlbk.domain.repository.RoomRepository

class AddUserToRoomUseCase (private val repository: RoomRepository) {
    operator fun invoke(code: String, user: User) = repository.addUserToRoom(code, user)
}