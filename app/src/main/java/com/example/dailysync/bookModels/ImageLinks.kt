package com.example.dailysync.bookModels

import com.google.firebase.database.PropertyName
import java.io.Serializable

data class ImageLinks (

    @PropertyName("smallThumbnail") val smallThumbnail : String,
    @PropertyName("thumbnail") val thumbnail : String
) : Serializable