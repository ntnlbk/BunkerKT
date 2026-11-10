package com.ntnlbk.data.repository

import com.ntnlbk.domain.model.Room
import com.ntnlbk.domain.repository.RoomRepository

class RoomRepositoryImpl: RoomRepository {
    override fun createRoom(code: String, hostNickname: String): Room {
        TODO("Not yet implemented")
    }

    override fun getRoom(code: String): Room? {
        TODO("Not yet implemented")
    }

    override fun addPlayerToRoom(code: String, playerNickname: String): Room {
        TODO("Not yet implemented")
    }

}