package com.example.dailysync.bookModels

import com.google.firebase.database.PropertyName
import java.io.Serializable

data class PanelizationSummary (

    @PropertyName("containsEpubBubbles") val containsEpubBubbles : Boolean,
    @PropertyName("containsImageBubbles") val containsImageBubbles : Boolean
) : Serializable{
    // Provide a no-argument constructor for Firebase deserialization
    constructor() : this(
        false,
        false
    )
}