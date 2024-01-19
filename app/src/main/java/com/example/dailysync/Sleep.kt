package com.example.dailysync

import java.time.Instant

data class Sleep(
    val bedTimeHour: Int,
    val bedTimeMin: Int,
    val awakeTimeHour: Int,
    val awakeTimeMin: Int,
    val timeSlept: Pair<Int,Int>,
    val date: Instant
)
