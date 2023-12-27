package com.example.dailysync.bookModels

import com.google.firebase.database.PropertyName
import java.io.Serializable

data class Book (

    @PropertyName("kind") val kind : String,
    @PropertyName("totalItems") val totalItems : Int,
    @PropertyName("items") var items : List<Items>
) : Serializable
