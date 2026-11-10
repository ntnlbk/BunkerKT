package com.ntnlbk.data.postgresql

import com.ntnlbk.data.dbmodel.PlayerTraitDBModel
import com.ntnlbk.data.postgresql.table.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Random
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class PlayerTraitDao(private val database: Database) {

    private fun getRandomTrait(
        table: Table,
        traitName: String,
        mapper: (ResultRow) -> PlayerTraitDBModel
    ): PlayerTraitDBModel {
        return transaction(database) {
            table.selectAll()
                .orderBy(Random())
                .limit(1)
                .map(mapper)
                .singleOrNull()
        } ?: throw Exception("Failed to load $traitName")
    }


    fun getBaggage(): PlayerTraitDBModel = getRandomTrait(BaggageTable, "Baggage") { row ->
        PlayerTraitDBModel(id = row[BaggageTable.id], value = row[BaggageTable.value])
    }

    fun getFact(): PlayerTraitDBModel = getRandomTrait(FactTable, "Fact") { row ->
        PlayerTraitDBModel(id = row[FactTable.id], value = row[FactTable.value])
    }

    fun getHealth(): PlayerTraitDBModel = getRandomTrait(HealthTable, "Health") { row ->
        PlayerTraitDBModel(id = row[HealthTable.id], value = row[HealthTable.value])
    }

    fun getHobby(): PlayerTraitDBModel = getRandomTrait(HobbyTable, "Hobby") { row ->
        PlayerTraitDBModel(id = row[HobbyTable.id], value = row[HobbyTable.value])
    }

    fun getPhobia(): PlayerTraitDBModel = getRandomTrait(PhobiaTable, "Phobia") { row ->
        PlayerTraitDBModel(id = row[PhobiaTable.id], value = row[PhobiaTable.value])
    }

    fun getProfession(): PlayerTraitDBModel = getRandomTrait(ProfessionTable, "Profession") { row ->
        PlayerTraitDBModel(id = row[ProfessionTable.id], value = row[ProfessionTable.value])
    }

    fun getSpecialCard(): PlayerTraitDBModel = getRandomTrait(SpecialCardTable, "Special Card") { row ->
        PlayerTraitDBModel(id = row[SpecialCardTable.id], value = row[SpecialCardTable.value])
    }
}