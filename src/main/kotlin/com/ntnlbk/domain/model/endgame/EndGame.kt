package com.ntnlbk.domain.model.endgame

data class EndGame(
    val survivors: List<SurvivorEnding>,
    val bunkerPart: EndGamePart,
    val outsidePart: EndGamePart?,
)
