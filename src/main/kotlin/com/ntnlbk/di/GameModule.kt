package com.ntnlbk.di


import com.ntnlbk.data.repository.GameRepositoryImpl
import com.ntnlbk.domain.repository.GameRepository
import com.ntnlbk.domain.usecase.game.EndGameUseCase
import com.ntnlbk.domain.usecase.game.EndVotingUseCase
import com.ntnlbk.domain.usecase.game.StartGameUseCase
import com.ntnlbk.domain.usecase.game.StartVotingUseCase
import org.koin.dsl.module

val gameModule = module {

    single<GameRepository> { GameRepositoryImpl(get(), get(), get(), get(), get()) }

    single { StartGameUseCase(get()) }
    single { StartVotingUseCase(get()) }
    single { EndVotingUseCase(get()) }
    single { EndGameUseCase(get()) }
}