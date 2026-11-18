package com.ntnlbk.di


import com.ntnlbk.presentation.dto.mapper.GameDTOMapper
import com.ntnlbk.presentation.dto.mapper.UserDTOMapper
import com.ntnlbk.presentation.dto.websocket.WebSocketManager
import org.koin.dsl.module

val presentationModule = module {

    factory { UserDTOMapper() }

    single { WebSocketManager() }

    factory { GameDTOMapper() }
}