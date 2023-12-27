package com.example.dailysync.bookModels

import com.google.firebase.database.PropertyName
import java.io.Serializable

data class AccessInfo (

    @PropertyName("country") val country : String,
    @PropertyName("viewability") val viewability : String,
    @PropertyName("embeddable") val embeddable : Boolean,
    @PropertyName("publicDomain") val publicDomain : Boolean,
    @PropertyName("textToSpeechPermission") val textToSpeechPermission : String,
    @PropertyName("epub") val epub : Epub,
    @PropertyName("pdf") val pdf : Pdf,
    @PropertyName("webReaderLink") val webReaderLink : String,
    @PropertyName("accessViewStatus") val accessViewStatus : String,
    @PropertyName("quoteSharingAllowed") val quoteSharingAllowed : Boolean
) : Serializable