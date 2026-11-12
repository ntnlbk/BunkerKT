package com.ntnlbk.di


import com.ntnlbk.presentation.dto.mapper.UserMapper
import com.ntnlbk.presentation.dto.websocket.WebSocketManager
import org.koin.dsl.module

val presentationModule = module {

    factory { UserMapper() }

    single { WebSocketManager() }
}