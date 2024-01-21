package com.example.dailysync.home.read

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dailysync.BookViewModel
import com.example.dailysync.bookModels.Items

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingSession(navController: NavHostController, bookViewModel: BookViewModel, item: Items) {
    Scaffold(
        modifier = Modifier.background(Color.White).padding(top = 15.dp),
        topBar = {
            TopBarSession(navController, item)
        },
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = Color.White
        ) {
            ReadingSessionBookDetails(item = item, bookViewModel, navController)
        }
    }
}

@Composable
fun TopBarSession(navController: NavHostController, item: Items?) {
    val titleIconModifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth()

    Box(
        Modifier
            .height(32.dp)
            .background(Color.White)) {

        Row(titleIconModifier, verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color(0xFF362305)
                ),
                enabled = true,
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        }
    }
}