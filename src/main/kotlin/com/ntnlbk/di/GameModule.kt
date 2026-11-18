package com.ntnlbk.di


import com.ntnlbk.data.repository.GameRepositoryImpl
import com.ntnlbk.domain.repository.GameRepository
import com.ntnlbk.domain.usecase.StartGameUseCase
import org.koin.dsl.module

val gameModule = module {

    single<GameRepository> { GameRepositoryImpl(get(), get(), get()) }

    single { StartGameUseCase(get()) }
}