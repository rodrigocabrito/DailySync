package com.example.dailysync.bookModels

import com.google.firebase.database.PropertyName
import java.io.Serializable

data class VolumeInfo (

    @PropertyName("title") val title : String,
    @PropertyName("subtitle") val subtitle : String,
    @PropertyName("authors") val authors : List<String>? = null,
    @PropertyName("publisher") val publisher : String,
    @PropertyName("publishedDate") val publishedDate : String,
    @PropertyName("description") val description : String,
    @PropertyName("industryIdentifiers") val industryIdentifiers : List<IndustryIdentifiers>,
    @PropertyName("readingModes") val readingModes : ReadingModes,
    @PropertyName("pageCount") val pageCount : Int,
    @PropertyName("printType") val printType : String,
    @PropertyName("categories") val categories : List<String>,
    @PropertyName("averageRating") val averageRating : Float,
    @PropertyName("ratingsCount") val ratingsCount : Int,
    @PropertyName("maturityRating") val maturityRating : String,
    @PropertyName("allowAnonLogging") val allowAnonLogging : Boolean,
    @PropertyName("contentVersion") val contentVersion : String,
    @PropertyName("panelizationSummary") val panelizationSummary : PanelizationSummary,
    @PropertyName("imageLinks") val imageLinks : ImageLinks? = null,
    @PropertyName("language") val language : String,
    @PropertyName("previewLink") val previewLink : String,
    @PropertyName("infoLink") val infoLink : String,
    @PropertyName("canonicalVolumeLink") val canonicalVolumeLink : String
) : Serializable{
    // Provide a no-argument constructor for Firebase deserialization
    constructor() : this(
        "",
        "",
        null,
        "",
        "",
        "",
        emptyList(),
        ReadingModes(),
        0,
        "",
        emptyList(),
        0.0f,
        0,
        "",
        false,
        "",
        PanelizationSummary(),
        null,
        "",
        "",
        "",
        ""
    )
}