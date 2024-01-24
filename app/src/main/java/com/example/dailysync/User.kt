package com.example.dailysync

import android.net.Uri

data class User(                    // TODO REMOVE EMAIL/PASSWORD
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val photoUri: Uri? = null,

    val runDailyGoal: Double = 0.0,
    val runWeeklyGoal: Double = 0.0,
    val runMonthlyGoal: Double = 0.0,

    val walkDailyGoal: Double = 0.0,
    val walkWeeklyGoal: Double = 0.0,
    val walkMonthlyGoal: Double = 0.0,

    val cycleDailyGoal: Double = 0.0,
    val cycleWeeklyGoal: Double = 0.0,
    val cycleMonthlyGoal: Double = 0.0,

    val dailyReadGoal: Int = 0,
    val weeklyReadGoal: Int = 0,
    val monthlyReadGoal: Int = 0,


)
