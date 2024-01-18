package com.example.dailysync.home.read

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dailysync.BookViewModel
import com.example.dailysync.R
import com.example.dailysync.bookModels.Items
import com.example.dailysync.bookModels.Status
import com.example.dailysync.navigation.Screens

// Composable
@Composable
fun MyLibrary(navController: NavController, bookViewModel: BookViewModel) {
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

    // footer
    Row(
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(80.dp)
                .background(Color(0xFFA2D6F0))
                .clickable {
                    navController.navigate(Screens.Home.route)
                }
                .border(1.dp, Color.Black)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center, // Center vertically
                horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.home_icon),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                        .size(35.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text( "Home")

            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .height(80.dp)
                .background(Color(0xFF2C8CBC))
                .clickable {
                    navController.navigate(Screens.Reports.route)
                }
                .border(1.dp, Color.Black)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center, // Center vertically
                horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.report_icon),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                        .size(35.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 10.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text( text ="Report")
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .height(80.dp)
                .background(Color(0xFFA2D6F0))
                .clickable {
                    navController.navigate(Screens.Community.route)
                }
                .border(1.dp, Color.Black)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center, // Center vertically
                horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.community_icon),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                        .size(45.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Text( text = "Community",
                    fontSize = 14.sp)
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .height(80.dp)
                .background(Color(0xFFA2D6F0))
                .clickable {
                    navController.navigate(Screens.Profile.route)
                }
                .border(1.dp, Color.Black)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp),
                verticalArrangement = Arrangement.Center, // Center vertically
                horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.profile_icon),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text( "Profile")
            }
        }
    }
}
@Composable
fun DisplayItemsList(navController: NavController, items: List<Items>, title: String) {
    if(items.isNotEmpty()) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 25.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp),
        ) {
            itemsIndexed(items) { _, item ->
                BookItemCard(
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