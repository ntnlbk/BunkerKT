package com.ntnlbk.domain.usecase

import com.ntnlbk.domain.model.RoomState
import com.ntnlbk.domain.repository.PlayerRepository

class VoteAgainstUseCase(private val repository: PlayerRepository) {
    operator fun invoke(game: RoomState.Game, playerNickname: String, targetPlayerNickname: String) =
        repository.voteAgainst(game, playerNickname, targetPlayerNickname)
}