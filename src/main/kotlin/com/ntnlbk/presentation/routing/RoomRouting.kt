package com.ntnlbk.presentation.routing

import com.ntnlbk.domain.model.User
import com.ntnlbk.domain.model.UserRole
import com.ntnlbk.domain.usecase.AddUserToRoomUseCase
import com.ntnlbk.domain.usecase.CreateRoomUseCase
import com.ntnlbk.domain.usecase.GetRoomUseCase
import com.ntnlbk.presentation.dto.api.CreateRoomRequest
import com.ntnlbk.presentation.dto.api.JoinRoomRequest
import com.ntnlbk.presentation.dto.api.RoomDefaultResponse
import com.ntnlbk.presentation.dto.api.RoomUsersResponse
import com.ntnlbk.presentation.dto.mapper.UserMapper
import com.ntnlbk.presentation.dto.model.GameStateDTO
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
    mapper: UserMapper,
    wsManager: WebSocketManager
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

    webSocket("/ws/{code}/{username}") {
        val roomCode = call.parameters["code"] ?: throw IllegalArgumentException("Missing room code")
        val username = call.parameters["username"] ?: throw IllegalArgumentException("Missing username")

        wsManager.addSession(roomCode, this)

        try {
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val rawText = frame.readText()

                    val clientMessage = try {
                        webSocketJson.decodeFromString<ClientMessage>(rawText)
                    } catch (e: Exception) {
                        println("Невалидный JSON от клиента: ${e.localizedMessage}")
                        continue
                    }

                    when (clientMessage) {
                        is ClientMessage.StartGame -> {
                            println("Игрок $username запросил старт для комнаты ${clientMessage.roomCode}")

                            val newState = GameStateDTO(code = clientMessage.roomCode)

                            val response: ServerMessage = ServerMessage.GameStarted(gameState = newState)

//                            val jsonResponse = webSocketJson.encodeToString(response)

                            wsManager.broadcast(roomCode, response)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            println("Соединение с $username оборвалось: ${e.localizedMessage}")
        } finally {
            wsManager.removeSession(roomCode, this)
        }
    }


}