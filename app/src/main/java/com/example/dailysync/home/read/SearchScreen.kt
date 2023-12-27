package com.example.dailysync.home.read

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.platform.LocalFocusManager
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

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(navController: NavHostController, bookViewModel: BookViewModel) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Scaffold(
            topBar = { SearchTopBar(navController, bookViewModel) },
        ) {
            BookList(navController, bookViewModel)

            if (bookViewModel.isLoading.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                ) {
                    CircularProgressIndicator()
                }
            }

            if(bookViewModel.emptySearchedResult) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
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
}

@Composable
fun BookList(navController: NavHostController, bookViewModel: BookViewModel) {
    when(bookViewModel.errorType.value) {
        Internet.ErrorType.NONE -> {
            GetBooks(navController, bookViewModel)
        }
        Internet.ErrorType.INTERNET -> {
            ErrorAlert(
                drawableRes = R.drawable.star_icon,
                text = "No internet connection",
            )
        }
        Internet.ErrorType.EXCEPTION -> {
            ErrorAlert(
                drawableRes = R.drawable.star_icon,
                text = bookViewModel.errorType.value.errorMsg,
            )
        }
        Internet.ErrorType.CUSTOM -> {
            ErrorAlert(
                drawableRes = R.drawable.headset_icon,
                text = "No books found",
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(navController: NavHostController, bookViewModel: BookViewModel) {
    var textState by remember { mutableStateOf("") }
    val focusRequester = FocusRequester()
    val focusManager = LocalFocusManager.current

        IconButton(onClick = {
            navController.popBackStack()
            bookViewModel.clearLoadItemsList()
        }) {
            Icon(Icons.Default.ArrowBack, null)
        }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = textState,
            onValueChange = {
                textState = it
            },
            placeholder = {
                Text(
                    text = "Search for title, author or ISBN"
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
        contentPadding = PaddingValues(horizontal = 25.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp),
        state = scrollState,
    ) {
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

            BookInfo(item = item) {
                // Handle click action, for example, navigate to HOME screen
                navController.navigate(Screens.Home.route)
            }
        }

        item {
            if (bookViewModel.isNextItemsLoading.value) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}


