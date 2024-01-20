package com.example.dailysync.home.read

import android.annotation.SuppressLint
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.dailysync.BookViewModel
import com.example.dailysync.R
import com.example.dailysync.bookModels.Items
import com.example.dailysync.bookModels.Status

private var openRemoveDialog by mutableStateOf(false)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(navController: NavHostController, bookViewModel: BookViewModel, item: Items) {
    val scrollState = rememberScrollState()
    val openDialog = remember { mutableStateOf(false) }
    val radioOptions = listOf("To Read", "Reading", "Finished")

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
            FloatingActionButton(
                modifier = Modifier.size(60.dp),
                onClick = {
                    openDialog.value = true
                },
                containerColor = Color(0xFFF5D4A2),
                contentColor = Color(0xFF362305),
            ) {
                if(item.status != null) {
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
        },
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
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(
                    text = "Select progress"
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
                                onClick = null
                            )
                            Text(
                                text = text,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                            // Additional options for marking the page when "Reading" is selected
                            if (text == "Reading" && selectedOption == "Reading") {
                                Spacer(modifier = Modifier.width(16.dp))
                                OutlinedTextField(
                                    value = if (currentPage == 0) "" else currentPage.toString(),
                                    onValueChange = { currentPage = if (it.isBlank()) -1 else it.toIntOrNull()?.coerceIn(-1, item?.volumeInfo?.pageCount ?: 0) ?: 0 },
                                    label = { Text("Current Page") },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Number
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
                            "To Read" -> item.status = Status.TO_READ
                            "Reading" -> {
                                item.status = Status.READING
                                // Retrieve the current page value from the text field
                                item.currentPage = currentPage
                            }
                            "Finished" -> item.status = Status.FINISHED
                        }
                        item.let { bookViewModel.updateStatus(it)}
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    if (openRemoveDialog) {
        AlertDialog(
            onDismissRequest = {
                openRemoveDialog = false
            },
            title = {
                Text(
                    text = "Remove",
                    style = TextStyle(
                        fontSize = 20.sp
                    ),
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Are you sure you want to remove this book?",
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
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openRemoveDialog = false
                    }
                ) {
                    Text("No")
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