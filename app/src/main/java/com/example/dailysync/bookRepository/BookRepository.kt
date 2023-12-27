package com.example.dailysync.bookRepository

import com.example.dailysync.bookModels.Book
import com.example.dailysync.bookModels.Items
import com.example.dailysync.bookModels.Status
import com.example.dailysync.bookapi.BookApi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.tasks.await

class BookRepository(private val bookApi: BookApi) {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("books")

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
        database.push().setValue(item).await()
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

    fun getItemsByStatus(status: Status, callback: (List<Items>) -> Unit) {
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
                }
            })
    }
    suspend fun deleteItemById(id: String) {
        database.child(id).removeValue().await()
    }
}