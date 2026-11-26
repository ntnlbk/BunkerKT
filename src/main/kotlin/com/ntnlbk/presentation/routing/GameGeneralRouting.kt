package com.ntnlbk.presentation.routing

import com.ntnlbk.domain.model.RoomState
import com.ntnlbk.domain.usecase.game.EndGameUseCase
import com.ntnlbk.domain.usecase.game.EndVotingUseCase
import com.ntnlbk.domain.usecase.room.GetRoomUseCase
import com.ntnlbk.domain.usecase.player.SetIsAliveTraitUseCase
import com.ntnlbk.domain.usecase.player.SetRandomTraitForPlayerUseCase
import com.ntnlbk.domain.usecase.player.SetStringAsTraitForPlayerUseCase
import com.ntnlbk.domain.usecase.player.SetTraitIsOpenUseCase
import com.ntnlbk.domain.usecase.game.StartVotingUseCase
import com.ntnlbk.domain.usecase.player.MixTraitForPlayersUseCase
import com.ntnlbk.domain.usecase.player.VoteAgainstUseCase
import com.ntnlbk.presentation.dto.mapper.GameDTOMapper
import com.ntnlbk.presentation.dto.websocket.ClientMessage
import com.ntnlbk.presentation.dto.websocket.ServerMessage
import com.ntnlbk.presentation.dto.websocket.ServerMessage.*
import com.ntnlbk.presentation.dto.websocket.WebSocketManager
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json

fun Route.gameGeneralRouting(
    wsManager: WebSocketManager,
    gameDTOMapper: GameDTOMapper,
    getRoomUseCase: GetRoomUseCase,
    setIsAliveTraitUseCase: SetIsAliveTraitUseCase,
    setTraitIsOpenUseCase: SetTraitIsOpenUseCase,
    startVotingUseCase: StartVotingUseCase,
    endVotingUseCase: EndVotingUseCase,
    voteAgainstUseCase: VoteAgainstUseCase,
    setRandomTraitForPlayerUseCase: SetRandomTraitForPlayerUseCase,
    setStringAsTraitForPlayerUseCase: SetStringAsTraitForPlayerUseCase,
    mixTraitForPlayersUseCase: MixTraitForPlayersUseCase,
    endGameUseCase: EndGameUseCase
) {

    val webSocketJson = Json {
        classDiscriminator = "type"
        ignoreUnknownKeys = true
        isLenient = true
    }

    webSocket("/ws/game/{code}/{nickname}") {
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
                        is ClientMessage.ReadyForGameStart -> {
                            val room = getRoomUseCase(roomCode)
                                ?: throw IllegalArgumentException("Unknown room code $roomCode")
                            val gameState =
                                room.roomState as? RoomState.Game
                                    ?: throw IllegalArgumentException(
                                        "Game has not started yet"
                                    )

                            val response: ServerMessage = GameState(
                                gameDTOMapper.mapDomainGameStateToGameStateDTO(gameState)
                            )
                            wsManager.broadcast(roomCode, response)
                        }

                        ClientMessage.StartGame -> {
                            // ignore
                        }

                        is ClientMessage.UpdatePlayerIsAlive -> {
                            val room = getRoomUseCase(roomCode)
                                ?: throw IllegalArgumentException("Unknown room code $roomCode")
                            val gameStateUpdated = setIsAliveTraitUseCase(
                                room.roomState as RoomState.Game,
                                clientMessage.nickname,
                                clientMessage.isAlive
                            )
                            val response: ServerMessage = GameState(
                                gameDTOMapper.mapDomainGameStateToGameStateDTO(gameStateUpdated)
                            )
                            wsManager.broadcast(roomCode, response)
                        }

                        is ClientMessage.UpdatePlayerTrait -> {
                            val room = getRoomUseCase(roomCode)
                                ?: throw IllegalArgumentException("Unknown room code $roomCode")
                            val gameStateUpdated = setTraitIsOpenUseCase(
                                room.roomState as RoomState.Game,
                                clientMessage.nickname,
                                clientMessage.traitName,
                                clientMessage.isOpen
                            )
                            val response: ServerMessage = GameState(
                                gameDTOMapper.mapDomainGameStateToGameStateDTO(gameStateUpdated)
                            )
                            wsManager.broadcast(roomCode, response)
                        }

                        ClientMessage.EndVoting -> {
                            val room = getRoomUseCase(roomCode)
                                ?: throw IllegalArgumentException("Unknown room code $roomCode")
                            val gameStateUpdated = endVotingUseCase(room.roomState as RoomState.Game)
                            val response: ServerMessage = GameState(
                                gameDTOMapper.mapDomainGameStateToGameStateDTO(gameStateUpdated)
                            )
                            wsManager.broadcast(roomCode, response)
                        }

                        ClientMessage.StartVoting -> {
                            val room = getRoomUseCase(roomCode)
                                ?: throw IllegalArgumentException("Unknown room code $roomCode")
                            val gameStateUpdated = startVotingUseCase(room.roomState as RoomState.Game)
                            val response: ServerMessage = GameState(
                                gameDTOMapper.mapDomainGameStateToGameStateDTO(gameStateUpdated)
                            )
                            wsManager.broadcast(roomCode, response)
                        }

                        is ClientMessage.VoteAgainst -> {
                            val room = getRoomUseCase(roomCode)
                                ?: throw IllegalArgumentException("Unknown room code $roomCode")
                            val gameStateUpdated = voteAgainstUseCase(
                                room.roomState as RoomState.Game,
                                clientMessage.nickname,
                                clientMessage.targetNickname
                            )
                            val response: ServerMessage = GameState(
                                gameDTOMapper.mapDomainGameStateToGameStateDTO(gameStateUpdated)
                            )
                            wsManager.broadcast(roomCode, response)
                        }

                        is ClientMessage.SetRandomTraitForPlayer -> {
                            val room = getRoomUseCase(roomCode)
                                ?: throw IllegalArgumentException("Unknown room code $roomCode")
                            val gameStateUpdated = setRandomTraitForPlayerUseCase(
                                room.roomState as RoomState.Game,
                                clientMessage.nickname,
                                clientMessage.traitName
                            )
                            val response: ServerMessage = GameState(
                                gameDTOMapper.mapDomainGameStateToGameStateDTO(gameStateUpdated)
                            )
                            wsManager.broadcast(roomCode, response)
                        }

                        is ClientMessage.SetStringAsTraitValueForPlayer -> {
                            val room = getRoomUseCase(roomCode)
                                ?: throw IllegalArgumentException("Unknown room code $roomCode")
                            val gameStateUpdated = setStringAsTraitForPlayerUseCase(
                                room.roomState as RoomState.Game,
                                clientMessage.nickname,
                                clientMessage.traitName,
                                clientMessage.value
                            )
                            val response: ServerMessage = GameState(
                                gameDTOMapper.mapDomainGameStateToGameStateDTO(gameStateUpdated)
                            )
                            wsManager.broadcast(roomCode, response)
                        }

                        is ClientMessage.MixTraitForPlayers -> {
                            val room = getRoomUseCase(roomCode)
                                ?: throw IllegalArgumentException("Unknown room code $roomCode")
                            val gameStateUpdated = mixTraitForPlayersUseCase(
                                room.roomState as RoomState.Game,
                                clientMessage.traitName,
                                clientMessage.nicknames
                            )
                            val response: ServerMessage = GameState(
                                gameDTOMapper.mapDomainGameStateToGameStateDTO(gameStateUpdated)
                            )
                            wsManager.broadcast(roomCode, response)
                        }

                        is ClientMessage.EndGame -> {
                            val room = getRoomUseCase(roomCode)
                                ?: throw IllegalArgumentException("Unknown room code $roomCode")
                            val final = endGameUseCase(room.roomState as RoomState.Game, true)
                        }
                    }
                }
            }
        } finally {
            wsManager.removeSession(roomCode, this)
        }

    }
}