package com.ntnlbk.presentation.routing

import com.ntnlbk.domain.model.User
import com.ntnlbk.domain.model.UserRole
import com.ntnlbk.domain.usecase.CreateRoomUseCase
import com.ntnlbk.presentation.dtomodel.CreateRoomRequest
import com.ntnlbk.presentation.dtomodel.RoomResponse
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.roomRouting(
    createRoomUseCase: CreateRoomUseCase
) {

    route("/api/rooms") {
        post("create") {
            val request = call.receive<CreateRoomRequest>()
            val room = createRoomUseCase(request.code, User(request.hostNickname, UserRole.GAME_MASTER))
            val response = RoomResponse(room.code)
            call.respond(response)
        }
    }

    get("/lobby/{code}") {
        call.respondFile(java.io.File("src/main/resources/static/lobby.html"))
    }
}