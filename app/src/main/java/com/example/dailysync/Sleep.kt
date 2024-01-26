package com.example.dailysync

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@IgnoreExtraProperties
@Parcelize
data class Sleep(
    @PropertyName("bedTimeHour") var bedTimeHour: Int,
    @PropertyName("bedTimeMin") val bedTimeMin: Int,
    @PropertyName("awakeTimeHour") val awakeTimeHour: Int,
    @PropertyName("awakeTimeMin") val awakeTimeMin: Int,
    @PropertyName("hourSlept") val hourSlept: Int,
    @PropertyName("minSlept") val minSlept: Int,
    @PropertyName("date") val date: Long
): Serializable, Parcelable {
    constructor() : this(
        0,
        0,
        0,
        0,
        0,
        0,
        0L
    )
}

