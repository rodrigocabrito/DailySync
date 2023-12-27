package com.example.dailysync.bookModels

import com.google.firebase.database.PropertyName
import java.io.Serializable

data class SaleInfo (

    @PropertyName("country") val country : String,
    @PropertyName("saleability") val saleability : String,
    @PropertyName("isEbook") val isEbook : Boolean,
    @PropertyName("listPrice") val listPrice : ListPrice,
    @PropertyName("retailPrice") val retailPrice : RetailPrice,
    @PropertyName("buyLink") val buyLink : String,
    @PropertyName("offers") val offers : List<Offers>
) : Serializable