package com.example.dailysync

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@IgnoreExtraProperties
@Parcelize
data class Exercise(
    @PropertyName("name") val name: String,
    @PropertyName("description") val description: String = "",
    @PropertyName("time") val time: Long,
    @PropertyName("averagePace") val averagePace: Float,
    @PropertyName("distance") val distance: Float,
    @PropertyName("image") val image: Boolean,
    @PropertyName("date") val date: Long,  // Use Long to represent date as milliseconds
    @PropertyName("type") var type: String = ""
) : Serializable, Parcelable {
    constructor() : this(
        "",
        "",
        0L,
        0f,
        0f,
        false,
        0L,
        ""
    )
}
