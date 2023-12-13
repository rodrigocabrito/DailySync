package com.example.dailysync

data class Exercise(
    val name: String,
    val description: String = "",
    val time: Long,
    val averagePace: Float,
    val distance: Float
)
