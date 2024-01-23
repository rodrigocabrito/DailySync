package com.example.dailysync.home.read

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.dailysync.BookViewModel
import com.example.dailysync.ErrorAlert
import com.example.dailysync.Internet
import com.example.dailysync.R
import com.example.dailysync.navigation.Screens

var textStateDuplicate: String = ""
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(navController: NavHostController, bookViewModel: BookViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Scaffold(
            topBar = {
                Column (modifier = Modifier.background(Color.White) ){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 15.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    navController.popBackStack()
                                    bookViewModel.clearLoadItemsList()
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    contentColor = Color(0xFF362305)
                                )
                            ) {
                                Icon(Icons.Default.ArrowBack, "Back")
                            }

                            IconButton(
                                onClick = {
                                    navController.navigate(Screens.Home.route)
                                    bookViewModel.clearLoadItemsList()
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    contentColor = Color(0xFF362305)
                                )
                            ) {
                                Icon(Icons.Default.Home, "Home")
                            }
                        }

                        Text(
                            text = "Find Books",
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 25.sp,
                                color = Color(0xFF362305)
                            )
                        )
                    }
                    SearchTopBar(bookViewModel)

                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.size(60.dp).border(2.dp, Color(0xFFC4AA83), RoundedCornerShape(15.dp)),
                    onClick = {
                        navController.navigate(Screens.MyLibrary.route)
                    },
                    containerColor = Color(0xFFF5D4A2),
                    contentColor = Color(0xFF362305),
                ) {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        painter = painterResource(id = R.drawable.library_icon),
                        tint = Color(0xFF362305),
                        contentDescription = "Swap reading status",
                    )
                }
            },
            content = {
                Column(modifier = Modifier.fillMaxSize().background(Color.White)){// background color do corpo sem nada
                    BookList(navController, bookViewModel)

                    if (bookViewModel.isLoading.value) {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center)
                                .background( Color.White),
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    if(bookViewModel.emptySearchedResult) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background( Color.White)
                        ) {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(vertical = 10.dp),
                                text = "Powered by Google",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                ),
                            )
                        }
                    }
                }

            }
        )
    }
}

@Composable
fun BookList(navController: NavHostController, bookViewModel: BookViewModel) {
    when(bookViewModel.errorType.value) {
        Internet.ErrorType.NONE -> {
            GetBooks(navController, bookViewModel)
        }
        Internet.ErrorType.INTERNET -> {
            ErrorAlert(
                drawableRes = R.drawable.ic_error,
                text = "No internet connection",
            )
        }
        Internet.ErrorType.EXCEPTION -> {
            ErrorAlert(
                drawableRes = R.drawable.ic_error,
                text = bookViewModel.errorType.value.errorMsg,
            )
        }
        Internet.ErrorType.CUSTOM -> {
            ErrorAlert(
                drawableRes = R.drawable.ic_reading,
                text = "No books found",
            )
        }
    }
}

@Composable
fun SearchTopBar(bookViewModel: BookViewModel) {
    var textState by remember { mutableStateOf("") }
    val focusRequester = FocusRequester()
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background( Color.White)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .background(
                    color = Color.White, // Set the background color of the TextField
                    shape = RoundedCornerShape(5.dp)
                ),
            value = textState,
            onValueChange = {
                textState = it
            },
            textStyle = TextStyle(color = Color(0xFF362305)),
            placeholder = {
                Text(
                    text = "Search for title, author or ISBN",
                    color = Color(0xFF362305)
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                if(textState.trim().isNotEmpty()) {
                    bookViewModel.emptySearchedResult = false
                    bookViewModel.clearLoadItemsList()

                    bookViewModel.loadItems(textState, 0)

                    focusManager.clearFocus()
                    textStateDuplicate = textState
                }
            }),
            singleLine = true
        )
    }
    DisposableEffect(Unit) {
        focusRequester.requestFocus()
        onDispose { }
    }
}


@Composable
fun GetBooks(navController: NavHostController, bookViewModel: BookViewModel) {
    val scrollState = rememberLazyListState()

    fun LazyListState.isScrolledToEnd() = layoutInfo.totalItemsCount > 0 &&
            layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

    LazyColumn(
        modifier = Modifier.background( Color.White),
        contentPadding = PaddingValues(horizontal = 25.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp),
        state = scrollState,
    ) {
        item {
            Spacer(modifier = Modifier.height(170.dp))
        }
        itemsIndexed(bookViewModel.items.value) { index, item ->
            val endOfListReached by remember {
                derivedStateOf {
                    scrollState.isScrolledToEnd()
                }
            }

            LaunchedEffect(endOfListReached) {
                if (endOfListReached && !bookViewModel.isNextItemsLoading.value) {
                    bookViewModel.loadNextItems(textStateDuplicate, index)
                }
            }

            BookItemCard(item = item) {
                navController.currentBackStackEntry?.savedStateHandle?.set("item", item)
                navController.navigate(Screens.BookDetails.route)
            }
        }

        item {
            if (bookViewModel.isNextItemsLoading.value) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background( Color.White),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}


