package com.ntnlbk.domain.repository

import com.ntnlbk.domain.model.endgame.EndGame
import com.ntnlbk.domain.model.endgame.EndGameContext

interface EndGameRepository {
    suspend fun generateStory(context: EndGameContext): EndGame
}