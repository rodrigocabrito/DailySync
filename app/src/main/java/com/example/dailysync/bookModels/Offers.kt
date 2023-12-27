package com.example.dailysync.bookModels

import com.google.firebase.database.PropertyName
import java.io.Serializable

data class Offers (

    @PropertyName("finskyOfferType") val finskyOfferType : Int,
    @PropertyName("listPrice") val listPrice : ListPrice,
    @PropertyName("retailPrice") val retailPrice : RetailPrice
) : Serializable