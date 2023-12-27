package com.example.dailysync.bookModels

import com.google.firebase.database.PropertyName
import java.io.Serializable

data class ReadingModes (

    @PropertyName("text") val text : Boolean,
    @PropertyName("image") val image : Boolean
) : Serializable