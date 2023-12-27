package com.example.dailysync.bookModels

import com.google.firebase.database.PropertyName
import java.io.Serializable

data class RetailPrice (

    @PropertyName("amountInMicros") val amountInMicros : Long,
    @PropertyName("currencyCode") val currencyCode : String
) : Serializable