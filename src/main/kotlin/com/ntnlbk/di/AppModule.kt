package com.ntnlbk.di

import com.ntnlbk.data.DummyRepositoryImpl
import com.ntnlbk.data.repository.RoomRepositoryImpl
import com.ntnlbk.domain.DummyRepository
import com.ntnlbk.domain.DummyUseCase
import com.ntnlbk.domain.repository.RoomRepository
import com.ntnlbk.domain.usecase.AddPlayerToRoomUseCase
import com.ntnlbk.domain.usecase.CreateRoomUseCase
import com.ntnlbk.domain.usecase.GetRoomUseCase
import org.koin.dsl.module

val appModule = module {
    single<DummyRepository> { DummyRepositoryImpl() }

    single { DummyUseCase(get()) }

    single<RoomRepository> { RoomRepositoryImpl() }

    single { CreateRoomUseCase(get()) }

    single { AddPlayerToRoomUseCase(get()) }

    single { GetRoomUseCase(get()) }
}