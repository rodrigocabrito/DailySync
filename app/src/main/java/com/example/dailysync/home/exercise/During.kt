package com.example.dailysync.home.exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dailysync.navigation.Screens
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DuringExercise(navController: NavController, categoryShow: Int, auth: FirebaseAuth) {

    var category by remember { mutableIntStateOf(categoryShow) }
    var textValue by remember { mutableStateOf("") }

    val title = when (category) {
        1 -> "Walk"
        2 -> "Run"
        else -> "Cycle"
    }

    var showDialog by remember { mutableStateOf(false) }

    // Press Yes (Pop Up)
    val confirmAction: () -> Unit = {
        showDialog = false
        navController.navigate(Screens.SaveExercise.route
            .replace(
                oldValue = "{category}",
                newValue = categoryShow.toString()
            ))
    }

    // Press Cancel (Pop Up)
    val cancelAction: () -> Unit = {
        showDialog = false
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

        Text(text = title)

        Spacer(modifier = Modifier.height(36.dp))

        // TODO GPS IMAGE

        Spacer(modifier = Modifier.height(380.dp))

        Box(
            modifier = Modifier
                .weight(3f)
                .width(350.dp)
                .padding(start = 20.dp, end = 20.dp, bottom = 15.dp)
                .background(Color.Gray, shape = RoundedCornerShape(8.dp)), // Set background color or other styling as needed
            contentAlignment = Alignment.Center
        ) {
            // TODO: Add real time info for the Exercise Info Box
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text("Time:")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Average Pace:")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Distance:")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Button(
                onClick = { //TODO STOP CLOCK
                    },
                modifier = Modifier
                    .height(50.dp)
                    .padding(end = 10.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(8.dp))
            ) {
                Text(text = "Pause")
            }

            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .height(50.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(8.dp))
            ) {
                Text(text = "Finish")
            }
        }

        // Show the AlertDialog Pop Up
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    // Handle dialog dismiss (e.g., when tapping outside the dialog)
                    showDialog = false
                },
                text = { Text("Do you want to finish your $title?") },
                confirmButton = {
                    TextButton(onClick = confirmAction) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = cancelAction) {
                        Text("Cancel")
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
            Button(
                onClick = { navController.navigate(Screens.Home.route)},
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
                    .background(Color.Gray)
            ) {
                Text("Home")
            }

            Button(
                onClick = { navController.navigate(Screens.Reports.route) },
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
                    .background(Color.Black)
            ) {
                Text("Report")
            }

            Button(
                onClick = { navController.navigate(Screens.Community.route) },
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
                    .background(Color.Gray)
            ) {
                Text("Community",
                    fontSize = 9.sp,
                    textAlign = TextAlign.Center
                )
            }

            Button(
                onClick = { navController.navigate(Screens.Profile.route) },
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
                    .background(Color.Black)
            ) {
                Text("Profile")
            }
        }

    }
}