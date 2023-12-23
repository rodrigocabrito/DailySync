package com.example.dailysync

data class SleepTarget(
    val bedTimeHour: Int = 0,
    val bedTimeMin: Int = 0,
    val awakeTimeHour: Int = 0,
    val awakeTimeMin: Int = 0,
    val timeTarget: Int = 0
)
