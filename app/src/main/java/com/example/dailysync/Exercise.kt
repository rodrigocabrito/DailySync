package com.example.dailysync

import java.time.Instant

data class Exercise(
    val name: String,
    val description: String = "",
    val time: Long,
    val averagePace: Float,
    val distance: Float,
    val image: Boolean,
    val date: Instant
)
