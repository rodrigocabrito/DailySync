package com.example.dailysync.bookModels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class ReadingSession(
    val itemId: String = "",
    val pagesRead: Int = 0,
    val durationMinutes: Int = 0
) : Parcelable, Serializable{
    constructor() : this(
        "",
        0,      // Set a default value for amountInMicros
        0       // Set a default value for currencyCode
    )
}

