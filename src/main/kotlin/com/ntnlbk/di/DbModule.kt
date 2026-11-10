package com.ntnlbk.di


import com.ntnlbk.data.mapper.PlayerTraitMapper
import com.ntnlbk.data.postgresql.DatabaseFactory
import com.ntnlbk.data.postgresql.PlayerTraitDao
import org.koin.dsl.module

val dbModule = module {

    single {
        DatabaseFactory().init()
    }

    single <PlayerTraitDao>{PlayerTraitDao(get())}

    factory { PlayerTraitMapper() }

}