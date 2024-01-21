package com.example.dailysync.home.read

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.dailysync.BookViewModel
import com.example.dailysync.bookModels.Items

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingSession(navController: NavHostController, bookViewModel: BookViewModel, item: Items) {

    var currentPage by remember { mutableIntStateOf(item.currentPage) }

    Scaffold(
        modifier = Modifier.background(Color.White),
        topBar = {
            TopBar(navController, item)
        },
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = Color.White
        ) {
            if (item.status != null) {
                AddedBookDetails(item = item)
            } else {
                BookDetails(item = item)
            }

        }
    }
}