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
) : Serializable{
    // Provide a no-argument constructor for Firebase deserialization
    constructor() : this(
        "",    // Set a default value for country
        "",    // Set a default value for viewability
        false, // Set a default value for embeddable
        false, // Set a default value for publicDomain
        "",    // Set a default value for textToSpeechPermission
        Epub(), // Assuming Epub has a no-argument constructor
        Pdf(),  // Assuming Pdf has a no-argument constructor
        "",    // Set a default value for webReaderLink
        "",    // Set a default value for accessViewStatus
        false  // Set a default value for quoteSharingAllowed
    )
}