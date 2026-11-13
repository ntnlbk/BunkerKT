package com.ntnlbk.data.repository

import com.ntnlbk.domain.model.Room
import com.ntnlbk.domain.model.RoomState
import com.ntnlbk.domain.model.User
import com.ntnlbk.domain.repository.RoomRepository
import java.util.concurrent.ConcurrentHashMap

class RoomRepositoryImpl : RoomRepository {

    private val rooms: ConcurrentHashMap<String, Room> = ConcurrentHashMap()

    override fun createRoom(code: String, gm: User): Room {
        val room = Room(
            code = code,
            hostNickname = gm.nickname,
            users = mutableListOf(gm),
            RoomState.Lobby
        )
        rooms[code] = room
        return room
    }

    override fun getRoom(code: String): Room? {
        return rooms[code]
    }

    override fun addUserToRoom(code: String, user: User): Room {
        val room = rooms[code] ?: throw IllegalArgumentException("Room with code $code doesn't exist")
        synchronized(room) {
            if (room.users.none { it.nickname == user.nickname }) {
                room.users.add(user)
            }
        }
        return room
    }

    override fun updateRoom(code: String, room: Room): Room {
        try {
            rooms[code] = room
            return room
        } catch (e: Exception) {
            throw NoSuchElementException("Error while updating the room: ${e.toString()}")
        }
    }

}