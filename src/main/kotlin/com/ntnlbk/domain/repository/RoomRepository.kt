package com.ntnlbk.domain.repository

import com.ntnlbk.domain.model.Biology
import com.ntnlbk.domain.model.Room

interface RoomRepository {

    fun createRoom(code: String, hostNickname: String): Room

    fun getRoom(code: String): Room?

    fun addPlayerToRoom(code: String, playerNickname: String): Room

}