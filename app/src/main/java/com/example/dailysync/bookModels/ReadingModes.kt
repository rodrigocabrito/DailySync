package com.example.dailysync.bookModels

import com.google.firebase.database.PropertyName
import java.io.Serializable

    data class ReadingModes (

        @PropertyName("text") val text : Boolean,
        @PropertyName("image") val image : Boolean
    ) : Serializable{
        // Provide a no-argument constructor for Firebase deserialization
        constructor() : this(
            false,   // Set a default value for text
            false    // Set a default value for image
        )
    }