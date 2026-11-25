package com.ntnlbk.domain.repository

import com.ntnlbk.domain.model.Room
import com.ntnlbk.domain.model.RoomState
import com.ntnlbk.domain.model.endgame.EndGame

interface GameRepository {
    fun startGame(room: Room): RoomState.Game
    fun startVoting(gameState: RoomState.Game): RoomState.Game
    fun endVoting(gameState: RoomState.Game): RoomState.Game
    suspend fun endGame(gameState: RoomState.Game): EndGame
}