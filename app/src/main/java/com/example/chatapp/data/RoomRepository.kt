package com.example.chatapp.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RoomRepository(private val firestore: FirebaseFirestore) {
    var id= 0
    suspend fun createRoom(name: String): Result<Unit> = try {
        id++
        val room = Room(name = name, id= id.toString())
        firestore.collection("rooms").add(room).await()
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun getRooms(): Result<List<Room>> = try {
        val querySnapshot = firestore.collection("rooms").get().await()
        val rooms = querySnapshot.documents.map { document ->
            document.toObject(Room::class.java)!!.copy(id = document.id)
        }
        id= rooms.size
        Result.Success(rooms)
    } catch (e: Exception) {
        Result.Error(e)
    }
}
