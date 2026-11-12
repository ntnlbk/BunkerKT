package com.ntnlbk.presentation.routing

import com.ntnlbk.domain.model.User
import com.ntnlbk.domain.model.UserRole
import com.ntnlbk.domain.usecase.AddUserToRoomUseCase
import com.ntnlbk.domain.usecase.CreateRoomUseCase
import com.ntnlbk.domain.usecase.GetRoomUseCase
import com.ntnlbk.presentation.dto.api.CreateRoomRequest
import com.ntnlbk.presentation.dto.api.RoomDefaultResponse
import com.ntnlbk.presentation.dto.api.JoinRoomRequest
import com.ntnlbk.presentation.dto.api.RoomUsersResponse
import com.ntnlbk.presentation.dto.mapper.UserMapper
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.roomRouting(
    createRoomUseCase: CreateRoomUseCase,
    getRoomUseCase: GetRoomUseCase,
    addUserToRoomUseCase: AddUserToRoomUseCase,
    mapper: UserMapper
) {

    route("/api/rooms") {
        post("create") {
            val request = call.receive<CreateRoomRequest>()
            val room = createRoomUseCase(request.code, User(request.hostNickname, UserRole.GAME_MASTER))
            val response = RoomDefaultResponse(room.code)
            call.respond(response)
        }
        post("join") {
            val request = call.receive<JoinRoomRequest>()
            val room = addUserToRoomUseCase(
                request.code,
                mapper.mapUserDTOToUser(request.user)
            )
            val response = RoomDefaultResponse(room.code)
            call.respond(response)
        }
    }

    get("/lobby/{code}") {
        call.respondFile(java.io.File("src/main/resources/static/lobby.html"))
    }

    get("/lobby/{code}/players") {
        val room = getRoomUseCase(
            call.parameters["code"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                "Missing room code"
            )
        )
        if (room == null) {
            call.respond(HttpStatusCode.NotFound, "Room not found")
            return@get
        }

        val response = RoomUsersResponse(
            room.users.map {
                mapper.mapUserToUserDTO(it)
            }
        )
        call.respond(HttpStatusCode.OK, response)
    }
}