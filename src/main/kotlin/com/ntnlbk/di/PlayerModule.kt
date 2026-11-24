package com.ntnlbk.di

import com.ntnlbk.data.repository.PlayerRepositoryImpl
import com.ntnlbk.domain.repository.PlayerRepository
import com.ntnlbk.domain.usecase.CreatePlayerUseCase
import com.ntnlbk.domain.usecase.GetRandomTraitUseCase
import com.ntnlbk.domain.usecase.SetIsAliveTraitUseCase
import com.ntnlbk.domain.usecase.SetRandomTraitForPlayerUseCase
import com.ntnlbk.domain.usecase.SetStringAsTraitForPlayerUseCase
import com.ntnlbk.domain.usecase.SetTraitIsOpenUseCase
import com.ntnlbk.domain.usecase.VoteAgainstUseCase
import org.koin.dsl.module

val playerModule = module {
    single<PlayerRepository> { PlayerRepositoryImpl(get(), get(), get(), get()) }
    factory { GetRandomTraitUseCase(get()) }
    factory { CreatePlayerUseCase(get()) }
    factory { SetIsAliveTraitUseCase(get()) }
    factory { SetTraitIsOpenUseCase(get()) }
    factory { VoteAgainstUseCase(get()) }
    factory { SetRandomTraitForPlayerUseCase(get()) }
    factory { SetStringAsTraitForPlayerUseCase(get()) }
}