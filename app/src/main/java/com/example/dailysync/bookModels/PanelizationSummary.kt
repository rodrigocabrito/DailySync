package com.example.dailysync.bookModels

import com.google.firebase.database.PropertyName
import java.io.Serializable

data class PanelizationSummary (

    @PropertyName("containsEpubBubbles") val containsEpubBubbles : Boolean,
    @PropertyName("containsImageBubbles") val containsImageBubbles : Boolean
) : Serializable