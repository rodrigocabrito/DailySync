package com.example.dailysync.home.read

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
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
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

    Scaffold(
        topBar = {
            TopBar(navController, item)
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.size(60.dp),
                onClick = {
                    openDialog.value = true
                }
            ) {
                if(item.status != null) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = R.drawable.ic_swap),
                        contentDescription = "",
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = ""
                    )
                }
            }
        },
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            BookDetails(item = item)
        }
    }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(
                    text = "Save To"
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
                                .height(46.dp)
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
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                            when (selectedOption) {
                                "To Read" -> Status.TO_READ
                                "Reading" -> Status.READING
                                "Finished" -> Status.FINISHED
                            }
                        item.let { bookViewModel.insertItem(it) }
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
    val appBarHorizontalPadding = 4.dp
    val titleIconModifier = Modifier
        .fillMaxHeight()
        .width(72.dp - appBarHorizontalPadding)
    val openMenu = remember { mutableStateOf(false) }

    Box(Modifier.height(32.dp)) {
        Row(titleIconModifier, verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                enabled = true,
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        }
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                text = "Details",
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp,
                    fontFamily = FontFamily.Cursive,
                ),
            )

        }


        if (item?.status != null) {
            IconButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = {
                    openMenu.value = true
                },
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
                ) {
                    DropdownMenuItem(
                        onClick = {
                            openMenu.value = false
                            openRemoveDialog = true
                        },
                        text = { Text(text = "Remove") }
                    )
                }
            }
        }
    }
}