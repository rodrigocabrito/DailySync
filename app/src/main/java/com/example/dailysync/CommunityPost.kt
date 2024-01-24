package com.example.dailysync

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@IgnoreExtraProperties
@Parcelize
data class CommunityPost(
    @PropertyName("userName") var userName: String,
    @PropertyName("type") val type: String,
    @PropertyName("profilePhoto") val profilePhoto: String,
    @PropertyName("date") val date: String,
    @PropertyName("exerciseType") val exerciseType: String,
    @PropertyName("distance") val distance: String,
    @PropertyName("time") val time: String,
    @PropertyName("rhythm") val rhythm: String,
    @PropertyName("bedTime") val bedTime: String,
    @PropertyName("wakeTime") val wakeTime: String,
    @PropertyName("sleepTime") val sleepTime: String,
    @PropertyName("bookName") val bookName: String,
    @PropertyName("pagesRead") val pagesRead: Int,
    @PropertyName("timeSpent") val timeSpent: String,

): Serializable, Parcelable {
    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        0,
        "",
    )
}