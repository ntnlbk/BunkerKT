package com.ntnlbk.data.repository

import com.ntnlbk.domain.model.GameStage
import com.ntnlbk.domain.model.Room
import com.ntnlbk.domain.model.RoomState
import com.ntnlbk.domain.model.UserRole
import com.ntnlbk.domain.repository.GameRepository
import com.ntnlbk.domain.usecase.CreatePlayerUseCase
import com.ntnlbk.domain.usecase.UpdateRoomUseCase

class GameRepositoryImpl(
    private val updateRoomUseCase: UpdateRoomUseCase,
    private val createPlayerUseCase: CreatePlayerUseCase
) : GameRepository {

    override fun startGame(room: Room): RoomState.Game {
        val players = room.users.filter { it.role == UserRole.PLAYER }.map {
            createPlayerUseCase(it.nickname)
        }
        val gameInitialState = RoomState.Game(
            players = players,
            GameStage.MAIN_PHASE,
            "test catastrophe description",
            "test bunker description",
            "test bunker inventory"
        )
        updateRoomUseCase(
            room.code,
            room.copy(roomState = gameInitialState)
        )
        return gameInitialState
    }

}