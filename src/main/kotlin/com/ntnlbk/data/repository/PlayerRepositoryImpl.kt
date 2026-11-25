package com.ntnlbk.data.repository

import com.ntnlbk.data.mapper.PlayerTraitMapper
import com.ntnlbk.data.postgresql.PlayerTraitDao
import com.ntnlbk.domain.model.Player
import com.ntnlbk.domain.model.PlayerTrait
import com.ntnlbk.domain.model.PlayerTraitName
import com.ntnlbk.domain.model.RoomState
import com.ntnlbk.domain.repository.PlayerRepository
import com.ntnlbk.domain.usecase.room.GetRoomUseCase
import com.ntnlbk.domain.usecase.room.UpdateRoomUseCase
import java.util.*
import kotlin.math.roundToInt

class PlayerRepositoryImpl(
    private val dao: PlayerTraitDao,
    private val mapper: PlayerTraitMapper,
    private val getRoomUseCase: GetRoomUseCase,
    private val updateRoomUseCase: UpdateRoomUseCase,
) : PlayerRepository {

    override fun createPlayer(nickname: String): Player {
        return Player(
            nickname = nickname,
            sex = getRandomTrait(PlayerTraitName.SEX),
            biology = getRandomTrait(PlayerTraitName.BIOLOGY),
            age = getRandomTrait(PlayerTraitName.AGE),
            profession = getRandomTrait(PlayerTraitName.PROFESSION),
            workExperience = getRandomTrait(PlayerTraitName.WORK_EXPERIENCE),
            health = getRandomTrait(PlayerTraitName.HEALTH),
            healthScore = getRandomTrait(PlayerTraitName.HEALTH_SCORE),
            hobby = getRandomTrait(PlayerTraitName.HOBBY),
            phobia = getRandomTrait(PlayerTraitName.PHOBIA),
            baggage = getRandomTrait(PlayerTraitName.BAGGAGE),
            factOne = getRandomTrait(PlayerTraitName.FACT_1),
            factTwo = getRandomTrait(PlayerTraitName.FACT_2),
            specialCards = listOf(
                getRandomTrait(PlayerTraitName.SPECIAL_CARD).value,
                getRandomTrait(PlayerTraitName.SPECIAL_CARD).value,
            )
        )
    }

    override fun getRandomTrait(traitName: PlayerTraitName): PlayerTrait {

        when (traitName) {
            PlayerTraitName.SEX -> {
                return PlayerTrait(
                    getRandomSex()
                )
            }

            PlayerTraitName.BIOLOGY -> {
                return PlayerTrait(
                    getRandomBiology()
                )
            }

            PlayerTraitName.AGE -> {
                return PlayerTrait(
                    value = getRandomAgeGaussian(
                        18,
                        90,
                        37.0,
                        12.0
                    ).toString()
                )
            }

            PlayerTraitName.PROFESSION -> {
                return mapper.mapDbModelToDomainModel(
                    dao.getProfession()
                )
            }

            PlayerTraitName.WORK_EXPERIENCE -> {
                return PlayerTrait(
                    value = getRandomAgeGaussian(
                        1,
                        18,
                        4.0,
                        4.0
                    ).toString()
                )
            }

            PlayerTraitName.HEALTH -> {
                return mapper.mapDbModelToDomainModel(
                    dao.getHealth()
                )
            }

            PlayerTraitName.HEALTH_SCORE -> {
                return PlayerTrait(
                    ((1..10).random() * 10).toString()
                )
            }

            PlayerTraitName.HOBBY -> {
                return mapper.mapDbModelToDomainModel(
                    dao.getHobby()
                )
            }

            PlayerTraitName.PHOBIA -> {
                return mapper.mapDbModelToDomainModel(
                    dao.getPhobia()
                )
            }

            PlayerTraitName.BAGGAGE -> {
                return mapper.mapDbModelToDomainModel(
                    dao.getBaggage()
                )
            }

            PlayerTraitName.FACT_1 -> {
                return mapper.mapDbModelToDomainModel(
                    dao.getFact()
                )
            }

            PlayerTraitName.FACT_2 -> {
                return mapper.mapDbModelToDomainModel(
                    dao.getFact()
                )
            }

            PlayerTraitName.SPECIAL_CARD -> {
                return mapper.mapDbModelToDomainModel(
                    dao.getSpecialCard()
                )
            }

            PlayerTraitName.BUNKER_DESCRIPTION -> {
                return mapper.mapDbModelToDomainModel(
                    dao.getBunkerDescription()
                )
            }

            PlayerTraitName.BUNKER_INVENTORY -> {
                return mapper.mapDbModelToDomainModel(
                    dao.getBunkerInventory()
                )
            }

            PlayerTraitName.CATASTROPHE_DESCRIPTION -> {
                return mapper.mapDbModelToDomainModel(
                    dao.getCatastropheDescription()
                )
            }
        }
    }

    override fun setIsAliveTrait(
        game: RoomState.Game,
        playerNickname: String,
        isAlive: Boolean
    ): RoomState.Game {
        var room = getRoomUseCase(game.code) ?: throw Exception("Cannot find room with code ${game.code}")
        room = updateRoomUseCase(
            game.code, room.copy(
                roomState = game.copy(
                    players = game.players.map {
                        if (it.nickname == playerNickname) {
                            it.copy(
                                isAlive = isAlive
                            )
                        } else {
                            it
                        }
                    }
                )
            ))
        return room.roomState as RoomState.Game
    }

    override fun setTraitIsOpen(
        game: RoomState.Game,
        playerNickname: String,
        trait: PlayerTraitName,
        isOpen: Boolean
    ): RoomState.Game {
        var room = getRoomUseCase(game.code) ?: throw Exception("Cannot find room with code ${game.code}")
        room = updateRoomUseCase(
            game.code, room.copy(
                roomState = game.copy(
                    players = game.players.map {
                        if (it.nickname == playerNickname) {
                            when (trait) {
                                PlayerTraitName.SEX -> it.copy(sex = it.sex.copy(isOpen = isOpen))
                                PlayerTraitName.BIOLOGY -> it.copy(biology = it.biology.copy(isOpen = isOpen))
                                PlayerTraitName.AGE -> it.copy(age = it.age.copy(isOpen = isOpen))
                                PlayerTraitName.PROFESSION -> it.copy(profession = it.profession.copy(isOpen = isOpen))
                                PlayerTraitName.WORK_EXPERIENCE -> it.copy(
                                    workExperience = it.workExperience.copy(
                                        isOpen = isOpen
                                    )
                                )

                                PlayerTraitName.HEALTH -> it.copy(health = it.health.copy(isOpen = isOpen))
                                PlayerTraitName.HEALTH_SCORE -> it.copy(healthScore = it.healthScore.copy(isOpen = isOpen))
                                PlayerTraitName.HOBBY -> it.copy(hobby = it.hobby.copy(isOpen = isOpen))
                                PlayerTraitName.PHOBIA -> it.copy(phobia = it.phobia.copy(isOpen = isOpen))
                                PlayerTraitName.BAGGAGE -> it.copy(baggage = it.baggage.copy(isOpen = isOpen))
                                PlayerTraitName.FACT_1 -> it.copy(factOne = it.factOne.copy(isOpen = isOpen))
                                PlayerTraitName.FACT_2 -> it.copy(factTwo = it.factTwo.copy(isOpen = isOpen))
                                PlayerTraitName.SPECIAL_CARD -> TODO()
                                PlayerTraitName.BUNKER_DESCRIPTION -> TODO()
                                PlayerTraitName.BUNKER_INVENTORY -> TODO()
                                PlayerTraitName.CATASTROPHE_DESCRIPTION -> TODO()
                            }
                        } else {
                            it
                        }
                    }
                )
            ))
        return room.roomState as RoomState.Game
    }

    override fun voteAgainst(
        game: RoomState.Game,
        playerNickname: String,
        targetPlayerNickname: String
    ): RoomState.Game {
        val voterIndex = game.players.indexOfFirst { it.nickname == playerNickname }
        val targetIndex = game.players.indexOfFirst { it.nickname == targetPlayerNickname }

        if (voterIndex == -1 || targetIndex == -1) throw Exception("Cannot find player $playerNickname")
        if (!game.players[voterIndex].isAlive || !game.players[targetIndex].isAlive) throw Exception("Invalid player $playerNickname")

        val targetSlot = targetIndex + 1

        val playersWithUpdatedChoice = game.players.map { player ->
            if (player.nickname == playerNickname) {
                player.copy(votesAgainst = targetSlot)
            } else {
                player
            }
        }

        val updatedPlayers = playersWithUpdatedChoice.mapIndexed { index, targetPlayer ->
            val currentSlot = index + 1
            val votersSlots = playersWithUpdatedChoice.mapIndexedNotNull { i, voter ->
                if (voter.isAlive && voter.votesAgainst == currentSlot) i + 1 else null
            }
            targetPlayer.copy(votesFromPlayers = votersSlots)
        }

        val updatedState = game.copy(players = updatedPlayers)

        val updatedRoom = updateRoomUseCase(
            updatedState.code,
            (getRoomUseCase(updatedState.code)
                ?: throw Exception("Cannot find room with code ${updatedState.code}")).copy(
                roomState = updatedState
            )
        )
        return updatedRoom.roomState as RoomState.Game

    }

    override fun setRandomTraitForPlayer(
        game: RoomState.Game,
        nickname: String,
        trait: PlayerTraitName
    ): RoomState.Game {

        when (trait) {
            PlayerTraitName.BUNKER_DESCRIPTION -> {
                val updatedState = game.copy(
                    bunkerDescription = getRandomTrait(PlayerTraitName.BUNKER_DESCRIPTION).value
                )
                var room =
                    getRoomUseCase(updatedState.code)
                        ?: throw Exception("Cannot find room with code ${updatedState.code}")
                room = updateRoomUseCase(room.code, room.copy(roomState = updatedState))
                return room.roomState as RoomState.Game
            }

            PlayerTraitName.BUNKER_INVENTORY -> {
                val updatedState = game.copy(
                    bunkerInventory = getRandomTrait(PlayerTraitName.BUNKER_INVENTORY).value
                )
                var room =
                    getRoomUseCase(updatedState.code)
                        ?: throw Exception("Cannot find room with code ${updatedState.code}")
                room = updateRoomUseCase(room.code, room.copy(roomState = updatedState))
                return room.roomState as RoomState.Game
            }

            PlayerTraitName.CATASTROPHE_DESCRIPTION -> {
                val updatedState = game.copy(
                    catastropheDescription = getRandomTrait(PlayerTraitName.CATASTROPHE_DESCRIPTION).value
                )
                var room =
                    getRoomUseCase(updatedState.code)
                        ?: throw Exception("Cannot find room with code ${updatedState.code}")
                room = updateRoomUseCase(room.code, room.copy(roomState = updatedState))
                return room.roomState as RoomState.Game
            }

            else -> {
                val updatedState = game.copy(
                    players = game.players.map {
                        if (it.nickname == nickname) {
                            when (trait) {
                                PlayerTraitName.SEX -> it.copy(sex = getRandomTrait(trait).copy(isOpen = it.sex.isOpen))
                                PlayerTraitName.BIOLOGY -> it.copy(biology = getRandomTrait(trait).copy(isOpen = it.biology.isOpen))
                                PlayerTraitName.AGE -> it.copy(age = getRandomTrait(trait).copy(isOpen = it.age.isOpen))
                                PlayerTraitName.PROFESSION -> it.copy(profession = getRandomTrait(trait).copy(isOpen = it.profession.isOpen))
                                PlayerTraitName.WORK_EXPERIENCE -> it.copy(
                                    workExperience = getRandomTrait(trait).copy(
                                        isOpen = it.workExperience.isOpen
                                    )
                                )

                                PlayerTraitName.HEALTH -> it.copy(health = getRandomTrait(trait).copy(isOpen = it.health.isOpen))
                                PlayerTraitName.HEALTH_SCORE -> it.copy(healthScore = getRandomTrait(trait).copy(isOpen = it.healthScore.isOpen))
                                PlayerTraitName.HOBBY -> it.copy(hobby = getRandomTrait(trait).copy(isOpen = it.hobby.isOpen))
                                PlayerTraitName.PHOBIA -> it.copy(phobia = getRandomTrait(trait).copy(isOpen = it.phobia.isOpen))
                                PlayerTraitName.BAGGAGE -> it.copy(baggage = getRandomTrait(trait).copy(isOpen = it.baggage.isOpen))
                                PlayerTraitName.FACT_1 -> it.copy(factOne = getRandomTrait(trait).copy(isOpen = it.factOne.isOpen))
                                PlayerTraitName.FACT_2 -> it.copy(factTwo = getRandomTrait(trait).copy(isOpen = it.factTwo.isOpen))
                                PlayerTraitName.SPECIAL_CARD -> throw Exception("Wrong trait name")
                                PlayerTraitName.BUNKER_DESCRIPTION -> throw Exception("Wrong trait name")
                                PlayerTraitName.BUNKER_INVENTORY -> throw Exception("Wrong trait name")
                                PlayerTraitName.CATASTROPHE_DESCRIPTION -> throw Exception("Wrong trait name")
                            }
                        } else {
                            it
                        }
                    }
                )
                var room =
                    getRoomUseCase(updatedState.code)
                        ?: throw Exception("Cannot find room with code ${updatedState.code}")
                room = updateRoomUseCase(room.code, room.copy(roomState = updatedState))
                return room.roomState as RoomState.Game
            }
        }
    }

    override fun setStringAsTraitForPlayer(
        game: RoomState.Game,
        playerNickname: String,
        trait: PlayerTraitName,
        traitValue: String
    ): RoomState.Game {
        when (trait) {
            PlayerTraitName.BUNKER_DESCRIPTION -> {
                val updatedState = game.copy(
                    bunkerDescription = traitValue
                )
                var room =
                    getRoomUseCase(updatedState.code)
                        ?: throw Exception("Cannot find room with code ${updatedState.code}")
                room = updateRoomUseCase(room.code, room.copy(roomState = updatedState))
                return room.roomState as RoomState.Game
            }

            PlayerTraitName.BUNKER_INVENTORY -> {
                val updatedState = game.copy(
                    bunkerInventory = traitValue
                )
                var room =
                    getRoomUseCase(updatedState.code)
                        ?: throw Exception("Cannot find room with code ${updatedState.code}")
                room = updateRoomUseCase(room.code, room.copy(roomState = updatedState))
                return room.roomState as RoomState.Game
            }

            PlayerTraitName.CATASTROPHE_DESCRIPTION -> {
                val updatedState = game.copy(
                    catastropheDescription = traitValue
                )
                var room =
                    getRoomUseCase(updatedState.code)
                        ?: throw Exception("Cannot find room with code ${updatedState.code}")
                room = updateRoomUseCase(room.code, room.copy(roomState = updatedState))
                return room.roomState as RoomState.Game
            }

            else -> {
                val updatedState = game.copy(
                    players = game.players.map {
                        if (it.nickname == playerNickname) {
                            when (trait) {
                                PlayerTraitName.SEX -> it.copy(sex = PlayerTrait(traitValue, it.sex.isOpen))
                                PlayerTraitName.BIOLOGY -> it.copy(biology = PlayerTrait(traitValue, it.biology.isOpen))
                                PlayerTraitName.AGE -> it.copy(age = PlayerTrait(traitValue, it.age.isOpen))
                                PlayerTraitName.PROFESSION -> it.copy(
                                    profession = PlayerTrait(
                                        traitValue,
                                        it.profession.isOpen
                                    )
                                )

                                PlayerTraitName.WORK_EXPERIENCE -> it.copy(
                                    workExperience = PlayerTrait(
                                        traitValue,
                                        it.workExperience.isOpen
                                    )
                                )

                                PlayerTraitName.HEALTH -> it.copy(health = PlayerTrait(traitValue, it.health.isOpen))
                                PlayerTraitName.HEALTH_SCORE -> it.copy(
                                    healthScore = PlayerTrait(
                                        traitValue,
                                        it.healthScore.isOpen
                                    )
                                )

                                PlayerTraitName.HOBBY -> it.copy(hobby = PlayerTrait(traitValue, it.hobby.isOpen))
                                PlayerTraitName.PHOBIA -> it.copy(phobia = PlayerTrait(traitValue, it.phobia.isOpen))
                                PlayerTraitName.BAGGAGE -> it.copy(baggage = PlayerTrait(traitValue, it.baggage.isOpen))
                                PlayerTraitName.FACT_1 -> it.copy(factOne = PlayerTrait(traitValue, it.factOne.isOpen))
                                PlayerTraitName.FACT_2 -> it.copy(factTwo = PlayerTrait(traitValue, it.factTwo.isOpen))
                                PlayerTraitName.SPECIAL_CARD -> throw Exception("Wrong trait name")
                                PlayerTraitName.BUNKER_DESCRIPTION -> throw Exception("Wrong trait name")
                                PlayerTraitName.BUNKER_INVENTORY -> throw Exception("Wrong trait name")
                                PlayerTraitName.CATASTROPHE_DESCRIPTION -> throw Exception("Wrong trait name")
                            }
                        } else {
                            it
                        }
                    }
                )
                var room =
                    getRoomUseCase(updatedState.code)
                        ?: throw Exception("Cannot find room with code ${updatedState.code}")
                room = updateRoomUseCase(room.code, room.copy(roomState = updatedState))
                return room.roomState as RoomState.Game
            }
        }
    }

    override fun mixTraitForPlayers(
        game: RoomState.Game,
        traitName: PlayerTraitName,
        playersNicknames: List<String>
    ): RoomState.Game {
        when (traitName) {
            PlayerTraitName.SPECIAL_CARD,
            PlayerTraitName.BUNKER_DESCRIPTION,
            PlayerTraitName.BUNKER_INVENTORY,
            PlayerTraitName.CATASTROPHE_DESCRIPTION -> throw Exception("Wrong trait name")
            else -> Unit
        }

        val targetSet = playersNicknames.toSet()
        if (targetSet.size < 2) {
            return game
        }

        fun extractTrait(player: Player): PlayerTrait = when (traitName) {
            PlayerTraitName.SEX -> player.sex
            PlayerTraitName.BIOLOGY -> player.biology
            PlayerTraitName.AGE -> player.age
            PlayerTraitName.PROFESSION -> player.profession
            PlayerTraitName.WORK_EXPERIENCE -> player.workExperience
            PlayerTraitName.HEALTH -> player.health
            PlayerTraitName.HEALTH_SCORE -> player.healthScore
            PlayerTraitName.HOBBY -> player.hobby
            PlayerTraitName.PHOBIA -> player.phobia
            PlayerTraitName.BAGGAGE -> player.baggage
            PlayerTraitName.FACT_1 -> player.factOne
            PlayerTraitName.FACT_2 -> player.factTwo
            else -> throw Exception("Wrong trait name")
        }

        fun applyTrait(player: Player, trait: PlayerTrait): Player = when (traitName) {
            PlayerTraitName.SEX -> player.copy(sex = trait)
            PlayerTraitName.BIOLOGY -> player.copy(biology = trait)
            PlayerTraitName.AGE -> player.copy(age = trait)
            PlayerTraitName.PROFESSION -> player.copy(profession = trait)
            PlayerTraitName.WORK_EXPERIENCE -> player.copy(workExperience = trait)
            PlayerTraitName.HEALTH -> player.copy(health = trait)
            PlayerTraitName.HEALTH_SCORE -> player.copy(healthScore = trait)
            PlayerTraitName.HOBBY -> player.copy(hobby = trait)
            PlayerTraitName.PHOBIA -> player.copy(phobia = trait)
            PlayerTraitName.BAGGAGE -> player.copy(baggage = trait)
            PlayerTraitName.FACT_1 -> player.copy(factOne = trait)
            PlayerTraitName.FACT_2 -> player.copy(factTwo = trait)
            else -> throw Exception("Wrong trait name")
        }

        val selectedPlayers = game.players.filter { it.nickname in targetSet }
        if (selectedPlayers.size < 2) {
            return game
        }

        val shuffledValues = selectedPlayers.map { extractTrait(it).value }.shuffled()
        val iterator = shuffledValues.iterator()

        val updatedPlayers = game.players.map { player ->
            if (player.nickname in targetSet && iterator.hasNext()) {
                val currentTrait = extractTrait(player)
                val nextValue = iterator.next()
                applyTrait(player, currentTrait.copy(value = nextValue))
            } else {
                player
            }
        }

        val updatedState = game.copy(players = updatedPlayers)
        var room = getRoomUseCase(updatedState.code)
            ?: throw Exception("Cannot find room with code ${updatedState.code}")
        room = updateRoomUseCase(room.code, room.copy(roomState = updatedState))
        return room.roomState as RoomState.Game
    }

    private fun getRandomAgeGaussian(
        min: Int,
        max: Int,
        targetMean: Double,
        stdDev: Double
    ): Int {
        val random = Random()
        var age: Double

        do {
            age = random.nextGaussian() * stdDev + targetMean
        } while (age < min || age > max)

        return age.roundToInt()
    }

    private fun getRandomSex(): String {
        return if (Random().nextBoolean()) "Male" else "Female"
    }

    private fun getRandomBiology(): String {
        return if (Random().nextBoolean()) "Fertile" else "Infertile"
    }
}