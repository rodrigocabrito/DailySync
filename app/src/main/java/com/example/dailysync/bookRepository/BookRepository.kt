package com.example.dailysync.bookRepository

import android.util.Log
import com.example.dailysync.bookModels.Book
import com.example.dailysync.bookModels.Items
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
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class BookRepository(private val bookApi: BookApi, auth: FirebaseAuth) {


    private var userId: String? = null
    private var database: DatabaseReference

    init {
        auth.addAuthStateListener { firebaseAuth ->
            // This block will be called when the authentication state changes
            userId = firebaseAuth.currentUser?.uid
            database = FirebaseDatabase.getInstance().reference.child("users").child(userId!!).child("books")
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
        userId?.let { Log.d("CARALHO", it) }
        val newItemReference = database.push()
        val newItem = item.copy(id = newItemReference.key ?: "")
        newItemReference.setValue(newItem).await()
    }

    suspend fun getItems(callback: (List<Items>) -> Unit) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.mapNotNull { it.getValue(Items::class.java) }
                callback(items)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
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
}