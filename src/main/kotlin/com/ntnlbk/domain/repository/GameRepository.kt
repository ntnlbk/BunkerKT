package com.ntnlbk.domain.repository

import com.ntnlbk.domain.model.Room
import com.ntnlbk.domain.model.RoomState

interface GameRepository {
    fun startGame(room: Room): RoomState.Game
}