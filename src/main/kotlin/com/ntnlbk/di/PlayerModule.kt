package com.ntnlbk.di

import com.ntnlbk.data.repository.PlayerRepositoryImpl
import com.ntnlbk.domain.repository.PlayerRepository
import com.ntnlbk.domain.usecase.player.CreatePlayerUseCase
import com.ntnlbk.domain.usecase.player.GetRandomTraitUseCase
import com.ntnlbk.domain.usecase.player.SetIsAliveTraitUseCase
import com.ntnlbk.domain.usecase.player.SetRandomTraitForPlayerUseCase
import com.ntnlbk.domain.usecase.player.SetStringAsTraitForPlayerUseCase
import com.ntnlbk.domain.usecase.player.SetTraitIsOpenUseCase
import com.ntnlbk.domain.usecase.player.VoteAgainstUseCase
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