package com.ntnlbk.data.repository

import com.ntnlbk.domain.model.endgame.EndGame
import com.ntnlbk.domain.model.endgame.EndGameContext
import com.ntnlbk.domain.repository.EndGameRepository

class EndGameRepositoryImpl : EndGameRepository {
    override suspend fun generateStory(context: EndGameContext): EndGame {
        TODO("Not yet implemented")
    }

}