package com.ntnlbk.di

import com.ntnlbk.data.repository.EndGameRepositoryImpl
import com.ntnlbk.domain.repository.EndGameRepository
import com.ntnlbk.domain.usecase.endgame.GenerateStoryUseCase
import org.koin.dsl.module

val endGameModule = module {

    single<EndGameRepository> { EndGameRepositoryImpl() }

    single { GenerateStoryUseCase(get()) }

}