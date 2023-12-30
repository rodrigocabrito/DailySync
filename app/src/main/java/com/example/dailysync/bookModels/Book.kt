package com.example.dailysync.bookModels

import com.google.firebase.database.PropertyName
import java.io.Serializable

data class Book (

    @PropertyName("kind") val kind : String,
    @PropertyName("totalItems") val totalItems : Int,
    @PropertyName("items") var items : List<Items>
) : Serializable{
    // Provide a no-argument constructor for Firebase deserialization
    constructor() : this(
        "",    // Set a default value for kind
        0,     // Set a default value for totalItems
        emptyList()  // Set a default value for items
    )
}
