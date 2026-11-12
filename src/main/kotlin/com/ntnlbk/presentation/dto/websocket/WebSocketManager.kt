package com.ntnlbk.presentation.dto.websocket

import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.websocket.Frame
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class WebSocketManager {

    @PublishedApi
    internal val rooms = ConcurrentHashMap<String, MutableSet<DefaultWebSocketServerSession>>()

    fun addSession(roomCode: String, session: DefaultWebSocketServerSession) {
        rooms.getOrPut(roomCode) { mutableSetOf() }.add(session)
    }

    fun removeSession(roomCode: String, session: DefaultWebSocketServerSession) {
        rooms[roomCode]?.remove(session)
        if (rooms[roomCode]?.isEmpty() == true) {
            rooms.remove(roomCode)
        }
    }

    suspend inline fun <reified T> broadcast(roomCode: String, message: T) {
        val jsonString = Json.encodeToString(message)
        rooms[roomCode]?.forEach { session ->
            try {
                session.outgoing.send(Frame.Text(jsonString))
            } catch (e: Exception) {

            }
        }
    }
}