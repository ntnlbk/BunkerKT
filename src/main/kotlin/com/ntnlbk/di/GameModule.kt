package com.ntnlbk.di


import com.ntnlbk.data.repository.GameRepositoryImpl
import com.ntnlbk.domain.repository.GameRepository
import com.ntnlbk.domain.usecase.EndVotingUseCase
import com.ntnlbk.domain.usecase.StartGameUseCase
import com.ntnlbk.domain.usecase.StartVotingUseCase
import org.koin.dsl.module

val gameModule = module {

    single<GameRepository> { GameRepositoryImpl(get(), get(), get(), get()) }

    single { StartGameUseCase(get()) }
    single { StartVotingUseCase(get()) }
    single { EndVotingUseCase(get()) }
}