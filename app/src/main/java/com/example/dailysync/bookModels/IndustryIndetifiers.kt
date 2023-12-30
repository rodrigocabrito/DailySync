package com.example.dailysync.bookModels

import com.google.firebase.database.PropertyName
import java.io.Serializable

data class IndustryIdentifiers (

    @PropertyName("type") val type : String,
    @PropertyName("identifier") val identifier : String
) : Serializable{
    // Provide a no-argument constructor for Firebase deserialization
    constructor() : this(
        "",    // Set a default value for type
        ""     // Set a default value for identifier
    )
}