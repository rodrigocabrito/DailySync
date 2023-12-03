package com.example.dailysync.home.exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dailysync.R
import com.example.dailysync.navigation.Screens
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SaveExercise(navController: NavController, categoryShow: Int, auth: FirebaseAuth) {

    var category by remember { mutableIntStateOf(categoryShow) }
    var textValue by remember { mutableStateOf("") }


    val title = when (category) {
        1 -> "Walk"
        2 -> "Run"
        else -> "Cycle"
    }

    var showDialogSave by remember { mutableStateOf(false) }
    var showDialogCancel by remember { mutableStateOf(false) }
    var showDialogCancelConfirmed by remember { mutableStateOf(false) }

    // Press OK (Pop Up)
    val confirmAction: () -> Unit = {
        showDialogSave = false
        showDialogCancelConfirmed = false
        navController.navigate(Screens.Home.route)
    }

    // Press OK (Pop Up)
    val confirmActionYes: () -> Unit = {
        showDialogCancel = false
        showDialogCancelConfirmed = true
    }
    // Press Cancel (Pop Up)
    val cancelAction: () -> Unit = {
        showDialogCancel = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // icons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Align text to the left
            Text(
                text = "IDK",
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1f)
            )

            // Align text to the right
            Text(
                text = "Notification Icon",
                textAlign = TextAlign.End,
                modifier = Modifier
                    .weight(1f)
            )
        }

        // body

        Text(text = "Save $title")

        Spacer(modifier = Modifier.height(36.dp))

        TextField(
            value = textValue,
            onValueChange = {
                textValue = it
            },
            label = { Text("Give a name to your $title") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )

        TextField(
            value = textValue,
            onValueChange = {
                textValue = it
            },
            label = { Text("How did it went? Share some details...") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )

        // TODO GPS IMAGE

        Spacer(modifier = Modifier.height(240.dp))

        Row {
            Box(
                modifier = Modifier
                    .weight(3f)
                    .height(105.dp)
                    .padding(start = 16.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(8.dp)), // Set background color or other styling as needed
                contentAlignment = Alignment.Center
            ) {
                // TODO: Add content for the Exercise Info Box
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text("Time:")
                    Text("Average Pace:")
                    Text("Distance:")
                }
            }

            // Column with Buttons
            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(start = 8.dp, end = 16.dp) // Add padding as needed
            ) {
                Button(
                    onClick = {
                        /*TODO*/
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .background(Color.Gray, shape = RoundedCornerShape(8.dp))
                ) {
                    Text(text = "Add Photos")
                }

                Spacer(modifier = Modifier.height(8.dp)) // Adjust spacing between buttons if needed

                Button(
                    onClick = {
                        /*TODO*/
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .background(Color.Gray, shape = RoundedCornerShape(8.dp))
                ) {
                    Text(text = "Take Photo")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Button(
                onClick = { showDialogCancel = true },
                modifier = Modifier
                    .height(50.dp)
                    .padding(end = 10.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(8.dp))
            ) {
                Text(text = "Cancel")
            }

            Button(
                onClick = { showDialogSave = true },
                modifier = Modifier
                    .height(50.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(8.dp))
            ) {
                Text(text = "Save")
            }
        }

        // Show the AlertDialog Pop Up
        if (showDialogSave) {
            AlertDialog(
                onDismissRequest = {
                    // Handle dialog dismiss (e.g., when tapping outside the dialog)
                    showDialogSave = false
                },
                text = { Text("Your $title was saved successfully!") },
                confirmButton = {
                    TextButton(onClick = confirmAction) {
                        Text("Ok")
                    }
                }
            )
        }

        // Show the AlertDialog Pop Up
        if (showDialogCancel) {
            AlertDialog(
                onDismissRequest = {
                    // Handle dialog dismiss (e.g., when tapping outside the dialog)
                    showDialogSave = false
                },
                text = { Text("Are you sure you want to cancel your $title?") },
                confirmButton = {
                    TextButton(onClick = confirmActionYes) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = cancelAction) {
                        Text("No")
                    }
                }
            )
        }

        if (showDialogCancelConfirmed) {
            AlertDialog(
                onDismissRequest = {
                    // Handle dialog dismiss (e.g., when tapping outside the dialog)
                    showDialogCancelConfirmed = false
                },
                text = { Text("Your $title was cancelled!") },
                confirmButton = {
                    TextButton(onClick = confirmAction) {
                        Text("Ok")
                    }
                }
            )
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
                    .background(Color(android.graphics.Color.parseColor("#A2D6F0")))
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
                            .size(50.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
                    .background(Color(android.graphics.Color.parseColor("#A2D6F0")))
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
                            .size(40.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
                    .background(Color(android.graphics.Color.parseColor("#A2D6F0")))
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
                            .size(60.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
                    .background(Color(android.graphics.Color.parseColor("#A2D6F0")))
                    .clickable {
                        navController.navigate(Screens.Profile.route)
                    }
                    .border(1.dp, Color.Black)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center, // Center vertically
                    horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.profile_icon),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier
                            .size(45.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

    }
}