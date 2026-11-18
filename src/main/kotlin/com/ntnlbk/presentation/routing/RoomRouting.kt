package com.ntnlbk.presentation.routing

import com.ntnlbk.domain.model.User
import com.ntnlbk.domain.model.UserRole
import com.ntnlbk.domain.usecase.AddUserToRoomUseCase
import com.ntnlbk.domain.usecase.CreateRoomUseCase
import com.ntnlbk.domain.usecase.GetRoomUseCase
import com.ntnlbk.domain.usecase.StartGameUseCase
import com.ntnlbk.presentation.dto.api.CreateRoomRequest
import com.ntnlbk.presentation.dto.api.JoinRoomRequest
import com.ntnlbk.presentation.dto.api.RoomDefaultResponse
import com.ntnlbk.presentation.dto.api.RoomUsersResponse
import com.ntnlbk.presentation.dto.mapper.UserDTOMapper
import com.ntnlbk.presentation.dto.websocket.ClientMessage
import com.ntnlbk.presentation.dto.websocket.ServerMessage
import com.ntnlbk.presentation.dto.websocket.WebSocketManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json

fun Route.roomRouting(
    createRoomUseCase: CreateRoomUseCase,
    getRoomUseCase: GetRoomUseCase,
    addUserToRoomUseCase: AddUserToRoomUseCase,
    mapper: UserDTOMapper,
    wsManager: WebSocketManager,
    startGameUseCase: StartGameUseCase
) {

    val webSocketJson = Json {
        classDiscriminator = "type"
        ignoreUnknownKeys = true
        isLenient = true
    }

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
                request.code, mapper.mapUserDTOToUser(request.user)
            )
            val response = RoomDefaultResponse(room.code)
            call.respond(response)
        }
    }

    get("/lobby/{code}") {
        val resourceStream = application.environment.classLoader.getResourceAsStream("static/lobby.html")
        if (resourceStream != null) {
            val bytes = resourceStream.readBytes()
            call.respondBytes(bytes, io.ktor.http.ContentType.Text.Html)
        } else {
            call.respond(io.ktor.http.HttpStatusCode.NotFound, "Lobby page not found")
        }
    }

    get("/lobby/{code}/gm") {
        val resourceStream = application.environment.classLoader.getResourceAsStream("static/gm.html")
        if (resourceStream != null) {
            val bytes = resourceStream.readBytes()
            call.respondBytes(bytes, io.ktor.http.ContentType.Text.Html)
        } else {
            call.respond(io.ktor.http.HttpStatusCode.NotFound, "Lobby page not found")
        }
    }

    get("/lobby/{code}/spectator") {
        val resourceStream = application.environment.classLoader.getResourceAsStream("static/spectator.html")
        if (resourceStream != null) {
            val bytes = resourceStream.readBytes()
            call.respondBytes(bytes, io.ktor.http.ContentType.Text.Html)
        } else {
            call.respond(io.ktor.http.HttpStatusCode.NotFound, "Lobby page not found")
        }
    }

    get("/lobby/{code}/player") {
        val resourceStream = application.environment.classLoader.getResourceAsStream("static/player.html")
        if (resourceStream != null) {
            val bytes = resourceStream.readBytes()
            call.respondBytes(bytes, io.ktor.http.ContentType.Text.Html)
        } else {
            call.respond(io.ktor.http.HttpStatusCode.NotFound, "Lobby page not found")
        }
    }

    get("/lobby/{code}/players") {
        val room = getRoomUseCase(
            call.parameters["code"] ?: return@get call.respond(
                HttpStatusCode.BadRequest, "Missing room code"
            )
        )
        if (room == null) {
            call.respond(HttpStatusCode.NotFound, "Room not found")
            return@get
        }

        val response = RoomUsersResponse(
            room.users.map {
                mapper.mapUserToUserDTO(it)
            })
        call.respond(HttpStatusCode.OK, response)
    }

    webSocket("/ws/lobby/{code}/{username}") {
        val roomCode = call.parameters["code"] ?: throw IllegalArgumentException("Missing room code")

        wsManager.addSession(roomCode, this)

        try {
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val rawText = frame.readText()

                    val clientMessage = try {
                        webSocketJson.decodeFromString<ClientMessage>(rawText)
                    } catch (e: Exception) {
                        continue
                    }

                    when (clientMessage) {
                        is ClientMessage.StartGame -> {
                            val room = getRoomUseCase(
                                roomCode
                            ) ?: throw IllegalArgumentException("Missing room with code $roomCode")
                            startGameUseCase(room)
                            val response: ServerMessage = ServerMessage.GameStarted
                            wsManager.broadcast(roomCode, response)
                        }

                        else -> {
                            // ignore
                        }
                    }
                }
            }
        } finally {
            wsManager.removeSession(roomCode, this)
        }
    }
}