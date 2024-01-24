package com.example.dailysync.home.read

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dailysync.BookViewModel
import com.example.dailysync.bookModels.Items
import com.example.dailysync.navigation.Screens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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

    var showDialogHomeCancel by remember { mutableStateOf(false) }
    var showDialogBackCancel by remember { mutableStateOf(false) }

    val cancelAction: () -> Unit = {
        showDialogHomeCancel = false
        showDialogBackCancel = false
    }

    Box(
        Modifier
            .height(32.dp)
            .background(Color.White)
    ) {

        Row(titleIconModifier, verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = {
                    showDialogBackCancel = true
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
            IconButton(
                onClick = {
                    showDialogHomeCancel = true
                },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color(0xFF362305)
                )
            ) {
                Icon(Icons.Default.Home, "Home")
            }
        }
    }
    if (showDialogHomeCancel) {
        AlertDialog(
            modifier = Modifier.border(2.dp, Color(0xFFC4AA83), RoundedCornerShape(25.dp)),
            containerColor = Color(0xFFF5D4A2),
            onDismissRequest = {
                showDialogHomeCancel = false
            },
            text = { Text("Are you sure you want to cancel your reading session?", color =Color(0xFF362305), fontWeight = FontWeight.Bold) },
            confirmButton = {
                TextButton(onClick = { navController.navigate(Screens.Home.route) }) {
                    Text("Yes", color =Color(0xFF362305))
                }
            },
            dismissButton = {
                TextButton(onClick = cancelAction) {
                    Text("No", color =Color(0xFF362305))
                }
            }
        )
    }
    if (showDialogBackCancel) {
        AlertDialog(
            modifier = Modifier.border(2.dp, Color(0xFFC4AA83), RoundedCornerShape(25.dp)),
            containerColor = Color(0xFFF5D4A2),
            onDismissRequest = {
                showDialogBackCancel = false
            },
            text = { Text("Are you sure you want to cancel your reading session?", color =Color(0xFF362305), fontWeight = FontWeight.Bold) },
            confirmButton = {
                TextButton(onClick = { navController.popBackStack() }) {
                    Text("Yes", color =Color(0xFF362305))
                }
            },
            dismissButton = {
                TextButton(onClick = cancelAction) {
                    Text("No", color =Color(0xFF362305))
                }
            }
        )
    }
}