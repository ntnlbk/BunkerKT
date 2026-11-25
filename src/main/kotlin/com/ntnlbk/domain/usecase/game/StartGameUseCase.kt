package com.ntnlbk.domain.usecase.game

import com.ntnlbk.domain.model.Room
import com.ntnlbk.domain.repository.GameRepository

class StartGameUseCase(private val repository: GameRepository) {
    operator fun invoke(room: Room) = repository.startGame(room)
}