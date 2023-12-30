package com.example.dailysync.bookModels

import com.google.firebase.database.PropertyName
import java.io.Serializable

data class Offers (

    @PropertyName("finskyOfferType") val finskyOfferType : Int,
    @PropertyName("listPrice") val listPrice : ListPrice,
    @PropertyName("retailPrice") val retailPrice : RetailPrice
) : Serializable{
    // Provide a no-argument constructor for Firebase deserialization
    constructor() : this(
        0,                // Set a default value for finskyOfferType
        ListPrice(),     // assuming ListPrice has a no-argument constructor
        RetailPrice()    // assuming RetailPrice has a no-argument constructor
    )
}