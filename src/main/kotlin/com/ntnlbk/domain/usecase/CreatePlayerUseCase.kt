package com.ntnlbk.domain.usecase

import com.ntnlbk.domain.repository.PlayerRepository

class CreatePlayerUseCase(private val repository: PlayerRepository) {
    operator fun invoke(nickname: String) = repository.createPlayer(nickname)
}