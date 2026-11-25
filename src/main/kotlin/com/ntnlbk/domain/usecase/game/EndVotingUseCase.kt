package com.ntnlbk.domain.usecase.game

import com.ntnlbk.domain.model.RoomState
import com.ntnlbk.domain.repository.GameRepository

class EndVotingUseCase(private val repository: GameRepository) {
    operator fun invoke(gameState: RoomState.Game) = repository.endVoting(gameState)
}