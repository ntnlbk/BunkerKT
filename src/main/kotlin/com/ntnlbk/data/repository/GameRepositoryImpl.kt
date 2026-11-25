package com.ntnlbk.data.repository

import com.ntnlbk.domain.model.*
import com.ntnlbk.domain.repository.GameRepository
import com.ntnlbk.domain.usecase.player.CreatePlayerUseCase
import com.ntnlbk.domain.usecase.player.GetRandomTraitUseCase
import com.ntnlbk.domain.usecase.room.GetRoomUseCase
import com.ntnlbk.domain.usecase.room.UpdateRoomUseCase

class GameRepositoryImpl(
    private val updateRoomUseCase: UpdateRoomUseCase,
    private val createPlayerUseCase: CreatePlayerUseCase,
    private val getRandomTraitUseCase: GetRandomTraitUseCase,
    private val getRoomUseCase: GetRoomUseCase,
) : GameRepository {

    override fun startGame(room: Room): RoomState.Game {
        val players = room.users.filter { it.role == UserRole.PLAYER }.map {
            createPlayerUseCase(it.nickname)
        }
        val gameInitialState = RoomState.Game(
            room.code,
            players = players,
            GameStage.MAIN_PHASE,
            getRandomTraitUseCase(PlayerTraitName.CATASTROPHE_DESCRIPTION).value,
            getRandomTraitUseCase(PlayerTraitName.BUNKER_DESCRIPTION).value,
            getRandomTraitUseCase(PlayerTraitName.BUNKER_INVENTORY).value
        )
        updateRoomUseCase(
            room.code, room.copy(roomState = gameInitialState)
        )
        return gameInitialState
    }

    override fun startVoting(gameState: RoomState.Game): RoomState.Game {
        val room = getRoomUseCase(gameState.code) ?: throw Exception("Room not found")
        val updatedState = gameState.copy(
            gameStage = GameStage.VOTING
        )
        val updatedRoom = updateRoomUseCase(updatedState.code, room.copy(roomState = updatedState))
        return updatedRoom.roomState as RoomState.Game
    }

    override fun endVoting(gameState: RoomState.Game): RoomState.Game {
        val room = getRoomUseCase(gameState.code) ?: throw Exception("Room not found")
        val updatedState = gameState.copy(
            gameStage = GameStage.MAIN_PHASE,
            players = gameState.players.map { it.copy(votesAgainst = -1, votesFromPlayers = listOf()) }
        )
        val updatedRoom = updateRoomUseCase(updatedState.code, room.copy(roomState = updatedState))
        return updatedRoom.roomState as RoomState.Game
    }

}