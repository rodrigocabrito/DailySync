package com.example.dailysync.bookModels

import com.google.firebase.database.PropertyName
import java.io.Serializable

data class IndustryIdentifiers (

    @PropertyName("type") val type : String,
    @PropertyName("identifier") val identifier : String
) : Serializable