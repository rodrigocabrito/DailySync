package com.example.dailysync.home.read

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.dailysync.BookViewModel
import com.example.dailysync.bookModels.Items
import com.example.dailysync.bookModels.Status
import com.example.dailysync.navigation.Screens

// Composable
@Composable
fun MyLibrary(navController: NavHostController, bookViewModel: BookViewModel) {
    var readingItems by remember { mutableStateOf<List<Items>>(emptyList()) }
    var toReadItems by remember { mutableStateOf<List<Items>>(emptyList()) }
    var finishedItems by remember { mutableStateOf<List<Items>>(emptyList()) }

    // Launch the coroutine to fetch items
    LaunchedEffect(Unit) {
        bookViewModel.getItemsByStatus(Status.READING) { items ->
            readingItems = items
        }
        bookViewModel.getItemsByStatus(Status.TO_READ) { items ->
            toReadItems = items
        }
        bookViewModel.getItemsByStatus(Status.FINISHED) { items ->
            finishedItems = items
        }
    }

    // Content of the composable
    when {
        readingItems.isNotEmpty() -> {
            DisplayItemsList(items = readingItems, title = "Reading", navController = navController)
        }
        toReadItems.isNotEmpty() -> {
            DisplayItemsList(items = toReadItems, title = "To Read", navController = navController)
        }
        finishedItems.isNotEmpty() -> {
            DisplayItemsList(items = finishedItems, title = "Finished", navController = navController)
        }
        else -> {
            // Handle case when all lists are empty
            // Display a message or UI for an empty state
        }
    }
}
@Composable
fun DisplayItemsList(navController: NavHostController, items: List<Items>, title: String) {
    if(items.isNotEmpty()) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 25.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp),
        ) {
            itemsIndexed(items) { _, item ->
                BookInfo(
                    item = item,
                    onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("item", item)
                        navController.navigate(Screens.BookDetails.route)
                    }
                )
                Text(text = title)
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            Text(
                text = "Click + to add books",
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
            )
        }
    }
}