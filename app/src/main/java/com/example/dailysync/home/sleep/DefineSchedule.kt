package com.example.dailysync.home.sleep

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dailysync.navigation.Screens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun DefineSleepSchedule(navController: NavController, auth: FirebaseAuth) {

    var targetValue by remember { mutableIntStateOf(0) }

    var showDialogSave by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    // Function to format hours and minutes
    fun formatHoursAndMinutes(valueInHalfHours: Int): String {
        val hours = valueInHalfHours / 2
        val minutes = (valueInHalfHours % 2) * 30
        if (minutes == 0) {
            return "$hours" + "h00"
        }
        return "$hours" + "h" + "$minutes"
    }

    // Press Cancel (Pop Up)
    val cancelAction: () -> Unit = {
        showDialog = false
    }

    // Press OK (Pop Up)
    val confirmAction: () -> Unit = {
        showDialogSave = true
        showDialog = false
    }

    // Press OK (Pop Up)
    val confirmActionSaved: () -> Unit = {
        showDialogSave = false
        navController.navigate(Screens.Home.route)
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // header

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

        Text(text = "Define Sleep Schedule")

        Spacer(modifier = Modifier.height(120.dp))

        // target
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.Gray, RoundedCornerShape(8.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Target",
                fontSize = 20.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 26.dp)
            )

            // Editable int value with buttons
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Button to decrease value
                    IconButton(
                        onClick = {
                            if (targetValue >= 1) {
                                targetValue--
                            }
                        }
                    ) {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease")
                    }

                    // Editable int value
                    Text(
                        text = formatHoursAndMinutes(targetValue),
                        fontSize = 20.sp,
                        color = Color.White
                    )

                    // Button to increase value
                    IconButton(
                        onClick = {
                            if (targetValue <= 46.5) {
                                targetValue++
                            }
                        }
                    ) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase")
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))

        // schedule
        Text(
            text = "Schedule",
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 35.dp)
        )

        Spacer(modifier = Modifier.height(5.dp))

        Box(
            modifier = Modifier
                .background(Color.Gray, RoundedCornerShape(8.dp))
                .height(130.dp)
                .width(356.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 35.dp, top = 20.dp)
            ) {
                Row {
                    Text(text = "Bed Time", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(140.dp))
                    Text(text = "Awake Time", fontSize = 12.sp)
                }

                // TODO GET THIS FROM EDIT BUTTON BELOW
                Row {
                    Text(text = "22:30", fontSize = 25.sp)
                    Spacer(modifier = Modifier.width(130.dp))
                    Text(text = "6:30", fontSize = 25.sp)
                }

                Spacer(modifier = Modifier.height(5.dp))

                TextButton(onClick = { /*TODO*/ }) {
                    Text(
                        text = "Edit",
                        fontSize = 22.sp,
                        textAlign = TextAlign.Start,
                        color = Color.Blue
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(150.dp))

        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .height(50.dp)
                .background(Color.Gray, shape = RoundedCornerShape(8.dp))
        ) {
            Text(text = "Save")
        }

        // Show the AlertDialog Pop Up
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    // Handle dialog dismiss (e.g., when tapping outside the dialog)
                    showDialog = false
                },
                text = { Text("Do you want to save this sleep schedule?") },
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

        // Show the AlertDialog Pop Up
        if (showDialogSave) {
            AlertDialog(
                onDismissRequest = {
                    // Handle dialog dismiss (e.g., when tapping outside the dialog)
                    showDialogSave = false
                },
                text = { Text("Your sleep schedule was saved successfully!") },
                confirmButton = {
                    TextButton(onClick = confirmActionSaved) {
                        Text("Ok")
                    }
                }
            )
        }
    }
}