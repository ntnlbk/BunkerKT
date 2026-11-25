package com.ntnlbk.domain.usecase.endgame

import com.ntnlbk.domain.model.endgame.EndGameContext
import com.ntnlbk.domain.repository.EndGameRepository

class GenerateStoryUseCase(private val repository: EndGameRepository) {
    suspend operator fun invoke(context: EndGameContext) = repository.generateStory(context)
}