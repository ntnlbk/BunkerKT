package com.ntnlbk.domain.repository

import com.ntnlbk.domain.model.Room
import com.ntnlbk.domain.model.RoomState

interface GameRepository {
    fun startGame(room: Room): RoomState.Game
    fun startVoting(gameState: RoomState.Game): RoomState.Game
    fun endVoting(gameState: RoomState.Game): RoomState.Game
}