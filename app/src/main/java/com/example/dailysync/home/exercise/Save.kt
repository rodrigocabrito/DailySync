package com.example.dailysync.home.exercise

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dailysync.R
import com.example.dailysync.navigation.Screens
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveExercise(navController: NavController, categoryShow: Int, auth: FirebaseAuth) {

    val category by remember { mutableIntStateOf(categoryShow) }
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

            label = { Text("Give a name to your $title", color = Color.Black) },
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(android.graphics.Color.parseColor("#A2F0C1"))),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),

            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)

        )


        TextField(
            value = textValue,
            onValueChange = {
                textValue = it
            },
            label = { Text("How did it went? Share some details...", color = Color.Black) },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color(android.graphics.Color.parseColor("#A2F0C1"))),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )

        // TODO GPS IMAGE

        Spacer(modifier = Modifier.height(180.dp))

        Row {
            Box(
                modifier = Modifier
                    .weight(3f)
                    .height(110.dp)
                    .padding(start = 16.dp)
                    .background(Color(android.graphics.Color.parseColor("#A2F0C1")), shape = RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                // TODO: GET INFO FROM 'DURING' PAGE
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
                    .padding(start = 8.dp, end = 16.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(
                            Color(android.graphics.Color.parseColor("#A2F0C1")),

                        )
                        .clickable {
                            // TODO ACCESS PHONE GALLERY
                        }
                        .drawBehind {
                            drawRoundRect(color = Color.Black, style = Stroke(width = 2f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                            )
                            )
                        }
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                    Text(
                        "Add Photos",
                        modifier = Modifier
                            .padding(horizontal = 5.dp) // Adjust the padding as needed
                            .fillMaxHeight()
                            .wrapContentSize(Alignment.Center)
                    )
                        Icon(
                            painter = painterResource(id = R.drawable.add_image_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 5.dp)
                                .size(40.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp)) // Adjust spacing between buttons if needed

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(
                            Color(android.graphics.Color.parseColor("#A2F0C1"))
                        )
                        .clickable {
                            // TODO ACCESS PHONE CAMERA
                        }
                        .drawBehind {
                            drawRoundRect(color = Color.Black, style = Stroke(width = 2f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                            )
                            )
                        }
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Take Photo",
                            modifier = Modifier
                                .padding(horizontal = 5.dp) // Adjust the padding as needed
                                .fillMaxHeight()
                                .wrapContentSize(Alignment.Center)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.add_photo_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 5.dp)
                                .size(40.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(60.dp))

        Row {
            Box(
                modifier = Modifier
                    .width(110.dp)
                    .height(50.dp)
                    .background(
                        Color(android.graphics.Color.parseColor("#A2F0C1")),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        showDialogCancel = true
                    }
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            ) {
                Text(
                    "Cancel",
                    modifier = Modifier
                        .padding(horizontal = 25.dp)
                        .fillMaxHeight()
                        .wrapContentSize(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .width(110.dp)
                    .height(50.dp)
                    .background(
                        Color(android.graphics.Color.parseColor("#47E285")),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        showDialogSave = true
                    }
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            ) {
                Text(
                    "Save",
                    modifier = Modifier
                        .padding(horizontal = 33.dp)
                        .fillMaxHeight()
                        .wrapContentSize(Alignment.Center)
                )
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
                    .background(Color(android.graphics.Color.parseColor("#A2D6F0")))
                    .clickable {
                        navController.navigate(Screens.Profile.route)
                    }
                    .border(1.dp, Color.Black)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top=10.dp),
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
}