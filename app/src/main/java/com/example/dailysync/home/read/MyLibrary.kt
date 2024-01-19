package com.example.dailysync.home.read

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.dailysync.BookViewModel
import com.example.dailysync.bookModels.Items
import com.example.dailysync.bookModels.Status
import com.example.dailysync.navigation.Screens

// Composable
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyLibrary(navController: NavHostController, bookViewModel: BookViewModel) {
    var readingItems by remember { mutableStateOf<List<Items>>(emptyList()) }
    var toReadItems by remember { mutableStateOf<List<Items>>(emptyList()) }
    var finishedItems by remember { mutableStateOf<List<Items>>(emptyList()) }
    var libraryItems by remember { mutableStateOf<List<Items>>(emptyList()) }
    var selectedCategory by remember { mutableStateOf("All") }

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
    libraryItems = readingItems + toReadItems + finishedItems
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Scaffold(
            topBar = {
                Column (modifier = Modifier.background(Color.White) ){
                    Row{
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                                bookViewModel.clearLoadItemsList()
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = Color(0xFF362305)
                            )
                        ) { Icon(Icons.Default.ArrowBack, "Back") }
                        Text(
                            text = "My Library",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color(0xFF362305)
                            )
                        )
                    }
                }
            },
            content = {


                Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(top =  70.dp)){// background color do corpo sem nada
                    // Content of the composable
                    Row (modifier = Modifier.padding(start = 15.dp, end = 15.dp)){
                        createCategoryButton("All", selectedCategory) {
                            selectedCategory = "All"
                            // Load or update items for "All" category
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        createCategoryButton("To Read", selectedCategory) {
                            selectedCategory = "To Read"
                            // Load or update items for "To Read" category
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        createCategoryButton("Finished", selectedCategory) {
                            selectedCategory = "Finished"
                            // Load or update items for "Finished" category
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        createCategoryButton("Reading", selectedCategory) {
                            selectedCategory = "Reading"
                            // Load or update items for "Reading" category
                        }
                    }
                    when {
                        toReadItems.isNotEmpty() -> {
                            DisplayItemsList(items = toReadItems, navController = navController, title = "To Read")
                        }
                        else -> {
                            // Handle case when all lists are empty
                            // Display a message or UI for an empty state
                        }
                    }
                }

            }
        )
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
                BookItemCard(
                    item = item,
                    onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("item", item)
                        navController.navigate(Screens.BookDetails.route)
                    }
                )
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

@Composable
fun createCategoryButton(
    category: String,
    selectedCategory: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(50.dp)
            .width(80.dp)
            .background(
                if (category == selectedCategory) Color(0xFF362305) else Color(0xFFF5D4A2),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .border(2.dp, color = if (category == selectedCategory) Color(0xFFF5D4A2) else Color(0xFF362305), shape = RoundedCornerShape(8.dp))
    ) {
        Text(
            category,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxHeight()
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center),
            color = if (category == selectedCategory) Color(0xFFF5D4A2) else Color(0xFF362305)
        )
    }
}