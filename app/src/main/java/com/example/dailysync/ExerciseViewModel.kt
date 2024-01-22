package com.example.dailysync

import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ExerciseViewModel : ViewModel() {
    private val _bitmap = mutableStateOf<Bitmap?>(null)
    val bitmap: State<Bitmap?> = _bitmap

    fun setMyBitmap(capturedBitmap: Bitmap) {
        _bitmap.value = capturedBitmap
    }
}