package com.ntnlbk.domain.usecase.game

import com.ntnlbk.domain.model.RoomState
import com.ntnlbk.domain.repository.GameRepository

class EndGameUseCase(private val repository: GameRepository) {
    suspend operator fun invoke(game: RoomState.Game, generateArts: Boolean) = repository.endGame(game, generateArts)
}