package com.example.dailysync.bookModels

import com.google.firebase.database.PropertyName
import java.io.Serializable

data class SearchInfo (

    @PropertyName("textSnippet") val textSnippet : String
) : Serializable{
    // Provide a no-argument constructor for Firebase deserialization
    constructor() : this("")
}