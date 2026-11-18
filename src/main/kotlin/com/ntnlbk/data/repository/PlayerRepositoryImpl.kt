package com.ntnlbk.data.repository

import com.ntnlbk.data.mapper.PlayerTraitMapper
import com.ntnlbk.data.postgresql.PlayerTraitDao
import com.ntnlbk.domain.model.Player
import com.ntnlbk.domain.model.PlayerTrait
import com.ntnlbk.domain.model.PlayerTraitName
import com.ntnlbk.domain.repository.PlayerRepository
import java.util.Random
import kotlin.math.roundToInt

class PlayerRepositoryImpl(
    private val dao: PlayerTraitDao,
    private val mapper: PlayerTraitMapper,
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
            factOne = getRandomTrait(PlayerTraitName.FACT),
            factTwo = getRandomTrait(PlayerTraitName.FACT),
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

            PlayerTraitName.FACT -> {
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