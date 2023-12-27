package com.example.dailysync.bookModels

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

enum class Status {
    TO_READ, READING, FINISHED
}

@IgnoreExtraProperties
@Parcelize
data class Items(
    var status: Status?,
    @PropertyName("kind") val kind: String = "",
    @PropertyName("etag") val etag: String = "",
    @PropertyName("selfLink") val selfLink: String = "",
    @PropertyName("volumeInfo") val volumeInfo: VolumeInfo,
    @PropertyName("saleInfo") val saleInfo: SaleInfo,
    @PropertyName("accessInfo") val accessInfo: AccessInfo,
    @PropertyName("searchInfo") val searchInfo: SearchInfo? = null,
) : Serializable, Parcelable