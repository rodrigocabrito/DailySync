package com.example.dailysync.bookModels

import com.google.firebase.database.PropertyName
import java.io.Serializable

data class Epub (

    @PropertyName("isAvailable") val isAvailable : Boolean,
    @PropertyName("acsTokenLink") val acsTokenLink : String
) : Serializable{
    // Provide a no-argument constructor for Firebase deserialization
    constructor() : this(
        false,
        ""
    )
}