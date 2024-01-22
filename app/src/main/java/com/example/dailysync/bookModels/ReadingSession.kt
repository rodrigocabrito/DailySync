package com.example.dailysync.bookModels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class ReadingSession(
    val itemId: String = "",
    val pagesRead: Int = 0,
    val durationMinutes: Int = 0,
    val date: Long = 0L
) : Parcelable, Serializable{
    constructor() : this(
        "",
        0,
        0,
        0L
    )
}

