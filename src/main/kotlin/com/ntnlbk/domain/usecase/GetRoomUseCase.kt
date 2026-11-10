package com.ntnlbk.domain.usecase

import com.ntnlbk.domain.repository.RoomRepository

class GetRoomUseCase(private val repository: RoomRepository) {
    operator fun invoke(code: String) = repository.getRoom(code)
}