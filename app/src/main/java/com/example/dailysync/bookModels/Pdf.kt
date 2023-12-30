package com.example.dailysync.bookModels

import com.google.firebase.database.PropertyName
import java.io.Serializable

data class Pdf (

    @PropertyName("isAvailable") val isAvailable : Boolean
) : Serializable{
    // Provide a no-argument constructor for Firebase deserialization
    constructor() : this(
        false,   // Set a default value for isAvailable
    )
}