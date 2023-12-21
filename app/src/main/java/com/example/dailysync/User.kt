package com.example.dailysync

import android.net.Uri

data class User(                    // TODO ADD FIELDS (STATS, PREFERENCES, ...)
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val photoUri: Uri? = null
)
