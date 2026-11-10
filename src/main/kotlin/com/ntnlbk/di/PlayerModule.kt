package com.ntnlbk.di

import com.ntnlbk.data.repository.PlayerRepositoryImpl
import com.ntnlbk.domain.repository.PlayerRepository
import com.ntnlbk.domain.usecase.CreatePlayerUseCase
import com.ntnlbk.domain.usecase.GetRandomTraitUseCase
import org.koin.dsl.module

val playerModule = module {
    single<PlayerRepository> { PlayerRepositoryImpl(get(), get())}
    factory { GetRandomTraitUseCase(get()) }
    factory { CreatePlayerUseCase(get()) }

}