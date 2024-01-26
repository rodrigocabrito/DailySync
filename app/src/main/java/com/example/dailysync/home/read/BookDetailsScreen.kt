package com.example.dailysync.home.read

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.dailysync.BookViewModel
import com.example.dailysync.R
import com.example.dailysync.bookModels.Items
import com.example.dailysync.bookModels.Status
import com.example.dailysync.navigation.Screens
import java.time.Instant

private var openRemoveDialog by mutableStateOf(false)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BookDetailsScreen(navController: NavHostController, bookViewModel: BookViewModel, item: Items) {
    val scrollState = rememberScrollState()
    val openDialog = remember { mutableStateOf(false) }
    val radioOptions = listOf("To Read", "Reading", "Finished")
    var expanded by remember { mutableStateOf(false) }
    var registerReadingSessionPopupVisible by remember { mutableStateOf(false) }

    fun formatStatus(status: String?): String {
        return when (status?.uppercase()) {
            "TO_READ" -> "To Read"
            "READING" -> "Reading"
            "FINISHED" -> "Finished"
            else -> radioOptions[0] // Default to the first option if status is null or not recognized
        }
    }

    val (selectedOption, onOptionSelected) = remember { mutableStateOf(formatStatus(item.status.toString())) }
    var currentPage by remember { mutableIntStateOf(item.currentPage) }



    Scaffold(
        modifier = Modifier.background(Color.White),
        topBar = {
            TopBar(navController, item)
        },
        floatingActionButton = {
            Column {
                if(item.status != null && item.status != Status.FINISHED) {
                    FloatingActionButton(
                        modifier = Modifier.size(60.dp).border(2.dp, Color(0xFFC4AA83), RoundedCornerShape(15.dp)),
                        onClick = {
                            expanded = true
                        },
                        containerColor = Color(0xFFF5D4A2),
                        contentColor = Color(0xFF362305),
                    ) {
                        Icon(
                            modifier = Modifier.size(25.dp),
                            painter = painterResource(id = R.drawable.play),
                            contentDescription = "Start or register reading session",
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(color = Color(0xFFF5D4A2)),
                        ) {
                            DropdownMenuItem(
                                onClick = { expanded = false
                                    registerReadingSessionPopupVisible = true},
                                modifier = Modifier.background(color = Color(0xFFF5D4A2)),
                                text = { Text(text = "Register Reading Session", color = Color(0xFF362305)) },
                            )

                            DropdownMenuItem(
                                onClick = {
                                    navController.currentBackStackEntry?.savedStateHandle?.set("item", item)
                                    navController.navigate(Screens.ReadingSession.route) },
                                modifier = Modifier.background(color = Color(0xFFF5D4A2)),
                                text = { Text(text = "Start Reading Session", color = Color(0xFF362305)) }
                            )
                        }

                        if (registerReadingSessionPopupVisible) {
                            RegisterReadingSessionPopup(
                                onDismiss = { registerReadingSessionPopupVisible = false },
                                bookViewModel, item, navController
                            )
                        }
                    }
                }

                // Add spacing between the two FloatingActionButton instances
                Spacer(modifier = Modifier.height(16.dp))

                FloatingActionButton(
                    modifier = Modifier.size(60.dp).border(2.dp, Color(0xFFC4AA83), RoundedCornerShape(15.dp)),
                    onClick = {
                        openDialog.value = true
                    },
                    containerColor = Color(0xFFF5D4A2),
                    contentColor = Color(0xFF362305),
                ) {
                    if (item.status != null) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(id = R.drawable.ic_swap),
                            contentDescription = "Swap reading status",
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add reading status"
                        )
                    }
                }
            }
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            color = Color.White
        ) {
            if(item.status != null){
                AddedBookDetails(item = item)
            }else{
                BookDetails(item = item)
            }

        }
    }

    if (openDialog.value) {
        AlertDialog(
            modifier = Modifier.border(2.dp, Color(0xFFC4AA83), RoundedCornerShape(25.dp)),
            containerColor = Color(0xFFF5D4A2),
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(
                    text = "Select Progress",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF362305)
                )
            },
            text = {
                Column(
                    modifier = Modifier.selectableGroup(),
                    verticalArrangement = Arrangement.Center,
                ) {
                    radioOptions.forEach { text ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(58.dp)
                                .selectable(
                                    selected = (text == selectedOption),
                                    onClick = { onOptionSelected(text) },
                                    role = Role.RadioButton,
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = (text == selectedOption),
                                onClick = null,
                                colors = RadioButtonDefaults.colors(
                                    unselectedColor = Color(0xFF362305),
                                    selectedColor = Color(0xFFC47E11)
                                )
                            )
                            Text(
                                text = text,
                                color = Color(0xFF362305),
                                modifier = Modifier.padding(start = 16.dp)
                            )
                            // Additional options for marking the page when "Reading" is selected
                            if (text == "Reading" && selectedOption == "Reading") {
                                Spacer(modifier = Modifier.width(16.dp))
                                OutlinedTextField(
                                    textStyle = TextStyle(
                                        color = Color(0xFF362305)
                                    ),
                                    value = if (currentPage == 0) "" else currentPage.toString(),
                                    onValueChange = { currentPage = if (it.isBlank()) 0 else it.toIntOrNull()?.coerceIn(-1, item?.volumeInfo?.pageCount ?: 0) ?: 0 },
                                    label = { Text("Current Page", color = Color(0xFFC47E11), fontSize = 12.sp) },
                                    placeholder = { currentPage.toString() },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Number
                                    ),
                                    colors = TextFieldDefaults.colors(
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color(0xFF362305),
                                        unfocusedLabelColor = Color(0xFF362305),
                                        focusedIndicatorColor = Color(0xFFC47E11),
                                        focusedLabelColor = Color(0xFFC47E11)

                                    ),
                                    modifier = Modifier.width(120.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        when (selectedOption) {
                            "To Read" -> {
                                item.status = Status.TO_READ
                                item.currentPage = 0
                            }
                            "Reading" -> {
                                item.status = Status.READING
                                item.currentPage = currentPage
                            }
                            "Finished" -> {
                                item.status = Status.FINISHED
                                item.currentPage = item.volumeInfo.pageCount
                            }
                        }
                        item.let { bookViewModel.updateStatus(it)}
                        navController.navigate(Screens.MyLibrary.route)
                    }
                ) {
                    Text("Save",color = Color(0xFF362305))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("Cancel",color = Color(0xFF362305))
                }
            }
        )
    }

    if (openRemoveDialog) {
        AlertDialog(
            modifier = Modifier.border(2.dp, Color(0xFFC4AA83), RoundedCornerShape(25.dp)),
            containerColor = Color(0xFFF5D4A2),
            onDismissRequest = {
                openRemoveDialog = false
            },
            title = {
                Text(
                    text = "Remove",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF362305)
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Are you sure you want to remove this book?",
                        color = Color(0xFF362305)
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        item.id.let { bookViewModel.deleteItem(it, navController) }
                        openRemoveDialog = false
                    }
                ) {
                    Text("Yes", color = Color(0xFF362305))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openRemoveDialog = false
                    }
                ) {
                    Text("No", color = Color(0xFF362305))
                }
            }
        )
    }

    if(bookViewModel.showRemovingDialog) {
        Dialog(
            onDismissRequest = { },
            content = {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .size(270.dp, 100.dp),
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 30.dp),
                    )
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "Removing",
                        style = TextStyle(
                            fontSize = 15.sp,
                        ),
                    )
                }
            }
        )
    }
}


@Composable
fun TopBar(navController: NavHostController, item: Items?) {
    val titleIconModifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth()
    val openMenu = remember { mutableStateOf(false) }

    Box(
        Modifier
            .height(32.dp)
            .background(Color.White).padding(top = 10.dp)) {

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

            IconButton(
                onClick = {
                    navController.navigate(Screens.Home.route)
                },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color(0xFF362305)
                )
            ) {
                Icon(Icons.Default.Home, "Home")
            }
        }

        if (item?.status != null) {
            IconButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = {
                    openMenu.value = true
                },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color(0xFF362305)
                ),
                enabled = true,
            ) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = null
                )
                DropdownMenu(
                    expanded = openMenu.value,
                    onDismissRequest = {
                        openMenu.value = false
                    },
                    modifier = Modifier.background(color = Color(0xFFF5D4A2))
                ) {
                    DropdownMenuItem(
                        onClick = {
                            openMenu.value = false
                            openRemoveDialog = true
                        },
                        modifier = Modifier.background(color = Color(0xFFF5D4A2)),
                        text = { Text(text = "Remove", color = Color(0xFF362305)) }
                    )
                }
            }
        }
    }
}

@Composable
fun RegisterReadingSessionPopup(
    onDismiss: () -> Unit,
    bookViewModel: BookViewModel, item: Items, navController: NavHostController
) {
    var textField1Value by remember { mutableStateOf(item.currentPage.toString()) }
    var textField3Value by remember { mutableStateOf("") }

    var timeSpentError by remember { mutableStateOf(false) }
    var nrPagesError by remember { mutableStateOf(false) }
    var lessPagesError by remember { mutableStateOf(false) }


    AlertDialog(
        modifier = Modifier.border(2.dp, Color(0xFFC4AA83), RoundedCornerShape(25.dp)),
        containerColor = Color(0xFFF5D4A2),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = "Register Reading Session",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFF362305)
            )
        },
        text = {
            Column {
                // Add your text fields and other UI elements here
                OutlinedTextField(
                    value = textField1Value,
                    textStyle = TextStyle(
                        color = Color(0xFF362305)
                    ),
                    placeholder = { Text("The current page you're on", color = Color.Gray) },
                    onValueChange = { textField1Value = it
                        nrPagesError = false
                        lessPagesError = false},
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    label = { Text("Last Page Read", color = Color(0xFF362305)) }
                )
                if (nrPagesError) {
                    Text(
                        text = "Field cannot be empty or with negative values",
                        fontSize = 11.sp,
                        color = Color.Red
                    )
                }
                if (lessPagesError) {
                    Text(
                        text = "Number of the page selected is less than it was registered before",
                        fontSize = 11.sp,
                        color = Color.Red
                    )
                }
                OutlinedTextField(
                    value = textField3Value,
                    textStyle = TextStyle(
                        color = Color(0xFF362305)
                    ),
                    onValueChange = { textField3Value = it },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    placeholder = { Text("Minutes spent reading",color = Color.Gray) },
                    label = { Text("Time spent reading (min)", color = Color(0xFF362305)) }
                )
                if (timeSpentError) {
                    Text(
                        text = "Field cannot be empty",
                        fontSize = 11.sp,
                        color = Color.Red
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if(textField1Value.isNotEmpty() && textField3Value.isNotEmpty() && textField1Value.toInt() > 0 && textField3Value.toInt() > 0 ){
                        if(textField1Value.toInt() > item.currentPage) {
                            val previousPage = item.currentPage
                            if (item.status == Status.TO_READ && textField1Value.toInt() > 0) {
                                item.status = Status.READING
                                item.currentPage = textField1Value.toInt()
                            } else if (textField1Value.toInt() >= item.volumeInfo.pageCount) {//Check if the input surpasses the amount of pages on the book
                                item.status = Status.FINISHED
                                item.currentPage = item.volumeInfo.pageCount
                            } else {
                                item.currentPage = textField1Value.toInt()
                            }
                            bookViewModel.registerReadingSession(
                                item,
                                textField1Value.toInt() - previousPage,
                                textField3Value.toInt(),
                                Instant.now().toEpochMilli()
                            )
                            onDismiss()
                            navController.currentBackStackEntry?.savedStateHandle?.set("item", item)
                            navController.navigate(Screens.BookDetails.route)
                        }else{
                            lessPagesError = true
                        }
                    }else{
                        nrPagesError = textField1Value.isBlank() || textField1Value.toInt() <= 0
                        timeSpentError = textField3Value.isBlank() || textField3Value.toInt() <= 0
                    }
                }
            ) {
                Text("Save",color = Color(0xFF362305))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Cancel",color = Color(0xFF362305))
            }
        }
    )
}