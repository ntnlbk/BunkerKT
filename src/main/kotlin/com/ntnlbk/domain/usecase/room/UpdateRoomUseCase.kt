package com.ntnlbk.domain.usecase.room

import com.ntnlbk.domain.model.Room
import com.ntnlbk.domain.repository.RoomRepository

class UpdateRoomUseCase(private val roomRepository: RoomRepository) {
    operator fun invoke(code: String, room: Room) = roomRepository.updateRoom(code, room)
}