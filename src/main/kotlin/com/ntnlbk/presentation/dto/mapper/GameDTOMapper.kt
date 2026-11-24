package com.ntnlbk.presentation.dto.mapper

import com.ntnlbk.domain.model.GameStage
import com.ntnlbk.domain.model.Player
import com.ntnlbk.domain.model.PlayerTrait
import com.ntnlbk.domain.model.RoomState
import com.ntnlbk.presentation.dto.model.GameStageDTO
import com.ntnlbk.presentation.dto.model.GameStateDTO
import com.ntnlbk.presentation.dto.model.PlayerDTO
import com.ntnlbk.presentation.dto.model.PlayerTraitDTO

class GameDTOMapper {

    fun mapPlayerTraitDomainToPlayerTraitDTO(playerTrait: PlayerTrait): PlayerTraitDTO {
        return PlayerTraitDTO(
            playerTrait.value,
            playerTrait.isOpen
        )
    }

    fun mapPlayerDomainToPlayerDTO(player: Player): PlayerDTO {
        return PlayerDTO(
            nickname = player.nickname,
            sex = mapPlayerTraitDomainToPlayerTraitDTO(player.sex),
            biology = mapPlayerTraitDomainToPlayerTraitDTO(player.biology),
            age = mapPlayerTraitDomainToPlayerTraitDTO(player.age),
            profession = mapPlayerTraitDomainToPlayerTraitDTO(player.profession),
            workExperience = mapPlayerTraitDomainToPlayerTraitDTO(player.workExperience),
            health = mapPlayerTraitDomainToPlayerTraitDTO(player.health),
            healthScore = mapPlayerTraitDomainToPlayerTraitDTO(player.healthScore),
            hobby = mapPlayerTraitDomainToPlayerTraitDTO(player.hobby),
            phobia = mapPlayerTraitDomainToPlayerTraitDTO(player.phobia),
            baggage = mapPlayerTraitDomainToPlayerTraitDTO(player.baggage),
            factOne = mapPlayerTraitDomainToPlayerTraitDTO(player.factOne),
            factTwo = mapPlayerTraitDomainToPlayerTraitDTO(player.factTwo),
            specialCards = player.specialCards,
            votesFromPlayers = player.votesFromPlayers,
            votesAgainst = player.votesAgainst,
            isAlive = player.isAlive
        )
    }

    fun mapDomainGameStateToGameStateDTO(gameState: RoomState.Game): GameStateDTO {
        return GameStateDTO(
            stage = when (gameState.gameStage) {
                GameStage.MAIN_PHASE -> GameStageDTO.MAIN_PHASE
                GameStage.VOTING -> GameStageDTO.VOTING
            },
            players = gameState.players.map { mapPlayerDomainToPlayerDTO(it) },
            catastropheDescription = gameState.catastropheDescription,
            bunkerDescription = gameState.bunkerDescription,
            bunkerInventory = gameState.bunkerInventory,
        )
    }

}