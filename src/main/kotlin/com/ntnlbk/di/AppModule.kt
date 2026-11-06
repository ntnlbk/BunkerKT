package com.ntnlbk.di

import com.ntnlbk.data.DummyRepositoryImpl
import com.ntnlbk.domain.DummyRepository
import com.ntnlbk.domain.DummyUseCase
import org.koin.dsl.module

val appModule = module {
    single<DummyRepository> { DummyRepositoryImpl() }

    single { DummyUseCase(get()) }
}