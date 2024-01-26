package com.example.dailysync.bookRepository

import com.example.dailysync.bookModels.Book
import com.example.dailysync.bookModels.Items
import com.example.dailysync.bookModels.ReadingSession
import com.example.dailysync.bookModels.Status
import com.example.dailysync.bookapi.BookApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class BookRepository(private val bookApi: BookApi, auth: FirebaseAuth) {


    private var userId: String? = null
    private var database: DatabaseReference

    init {
        auth.addAuthStateListener { firebaseAuth ->
            // This block will be called when the authentication state changes
            userId = firebaseAuth.currentUser?.uid

            // Update the database reference if userId is not null
            database = if (userId != null) {
                FirebaseDatabase.getInstance().reference.child("users").child(userId!!).child("books")
            } else {
                // Handle the case where userId is null (user is not authenticated)
                FirebaseDatabase.getInstance().reference
            }
        }

        // Initialize the database reference, but userId is initially null
        database = FirebaseDatabase.getInstance().reference
    }

    suspend fun getBooksList(query: String, startIndex: Int, callback: (ApiResponse<Book?>) -> Unit) {
        val result = bookApi.getBooks(
            query = query,
            startIndex = startIndex,
            maxResults = 20,
            apiKey = "AIzaSyDhZ_To4Ij9b4KVRORfdZDYFOVOeZU3d5A",
        )
        callback(result)
    }

    suspend fun insertItem(item: Items) {
        val newItemReference = database.push()
        val newItem = item.copy(id = newItemReference.key ?: "")
        newItemReference.setValue(newItem).await()
    }

    suspend fun deleteAll() {
        database.removeValue().await()
    }

    suspend fun getItemsByStatus(status: Status, callback: (List<Items>) -> Unit) {
        withContext(Dispatchers.IO) {
            database
                .orderByChild("status")
                .equalTo(status.name)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val itemsByStatus = snapshot.children.mapNotNull { it.getValue(Items::class.java) }
                        callback(itemsByStatus)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error
                        // You might want to pass an empty list or some default value to the callback
                        callback(emptyList())
                    }
                })
        }
    }
    suspend fun deleteItemById(id: String) {
        database.child(id).removeValue().await()
    }

    suspend fun checkItemExists(id: String): Boolean {
        return suspendCancellableCoroutine { continuation ->
            val databaseReference = database.child(id)

            val eventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val exists = dataSnapshot.exists()
                    continuation.resume(exists)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    continuation.resume(false)
                }
            }

            databaseReference.addListenerForSingleValueEvent(eventListener)

            // Cancellation handling
            continuation.invokeOnCancellation {
                databaseReference.removeEventListener(eventListener)
            }
        }
    }

    suspend fun updateItem(item: Items) {
        userId?.let {
            val itemId = item.id
            database.child(itemId).setValue(item).await()
        }
    }

    suspend fun insertReadingSession(item: Items, session: ReadingSession) {
        userId?.let {
            val sessionReference = database.child("readingSessions").push()

            // Set the item ID in the session
            sessionReference.child("itemId").setValue(item.id)

            // Set other session details
            sessionReference.child("pagesRead").setValue(session.pagesRead)
            sessionReference.child("durationMinutes").setValue(session.durationMinutes)
            sessionReference.child("date").setValue(session.date)
        }
    }

    suspend fun getReadingSessionsForItem(item: Items): List<ReadingSession> {
        val dataSnapshot = database.child("readingSessions")
            .orderByChild("itemId")
            .equalTo(item.id)
            .get()
            .await()

        val sessions = dataSnapshot.children.mapNotNull { snapshot ->
            snapshot.getValue(ReadingSession::class.java)
        }

        return sessions
    }

    suspend fun deleteReadingSessionsByItemId(itemId: String) {
        userId?.let {
            val dataSnapshot = database.child("readingSessions")
                .orderByChild("itemId")
                .equalTo(itemId)
                .get()
                .await()

            dataSnapshot.children.forEach { snapshot ->
                // Delete each reading session
                snapshot.ref.removeValue().await()
            }
        }
    }
}