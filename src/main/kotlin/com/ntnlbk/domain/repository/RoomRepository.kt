package com.ntnlbk.domain.repository

import com.ntnlbk.domain.model.Room
import com.ntnlbk.domain.model.User

interface RoomRepository {

    fun createRoom(code: String, gm: User): Room

    fun getRoom(code: String): Room?

    fun addUserToRoom(code: String, user: User): Room

    fun updateRoom(code: String, room: Room): Room

}