package com.example.dailysync.home.sleep

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dailysync.navigation.Screens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun EditSleepSchedule(navController: NavController, auth: FirebaseAuth, targetShow: String) {

    val hours = getHourFromTarget(targetShow)
    val minutes = getMinuteFromTarget(targetShow)

    val targetHours by remember { mutableIntStateOf(hours) }
    val targetMinutes by remember { mutableIntStateOf(minutes) }

    var selectedHourBedTime by remember { mutableIntStateOf(0) }
    var selectedMinBedTime by remember { mutableIntStateOf(0) }

    var showDialogSave by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    var selectedHourAwakeTime by remember { mutableIntStateOf(selectedHourBedTime + targetHours) }
    var selectedMinAwakeTime by remember { mutableIntStateOf(selectedMinBedTime + targetMinutes) }

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

        val targetToPassBack = formatTarget(targetHours, targetMinutes)

        val minBedTime = if (selectedMinBedTime == 0) {
            "00"
        } else {
            selectedMinBedTime.toString()
        }

        val minAwakeTime = if (selectedMinAwakeTime == 0) {
            "00"
        } else {
            selectedMinAwakeTime.toString()
        }

        val hourBedTime = if (selectedHourBedTime < 0) {
            (24 + selectedHourBedTime).toString()
        } else {
            selectedHourBedTime.toString()
        }

        val hourAwakeTime = if (selectedHourAwakeTime < 0) {
            (24 + selectedHourAwakeTime).toString()
        } else {
            selectedHourAwakeTime.toString()
        }

        val route = Screens.DefineSleepSchedule.route.replace(
            oldValue = "{bedTime}",
            newValue = "$hourBedTime:$minBedTime"
        )
            .replace(
                oldValue = "{awakeTime}",
                newValue = "$hourAwakeTime:$minAwakeTime"
            )
            .replace(
                oldValue = "{target}",
                newValue = "$targetToPassBack"
            )
        println("Navigate to: $route")
        navController.navigate(route)
    }

    // header

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

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Edit Sleep Schedule")

        Spacer(modifier = Modifier.height(36.dp))

        // bed time
        
        Text(text = "Bed Time",
            Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp))

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.Transparent)
        ){
            //hours
            Column (
                modifier = Modifier
                    .background(
                        Color.Yellow,
                        RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                    )
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    IconButton(
                        onClick = {
                            selectedHourBedTime--
                            selectedHourAwakeTime--
                                  },
                        modifier = Modifier
                            .background(Color.LightGray)
                            .fillMaxWidth()
                            .height(27.dp)
                            .weight(1f)
                    ) {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease")
                    }

                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(text = formatHour(selectedHourBedTime - 2), Modifier.alpha(0.2f), fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatHour(selectedHourBedTime - 1), Modifier.alpha(0.5f), fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatHour(selectedHourBedTime), Modifier.background(Color.LightGray), fontSize = 20.sp) //selected
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatHour(selectedHourBedTime + 1), Modifier.alpha(0.5f), fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatHour(selectedHourBedTime + 2), Modifier.alpha(0.2f), fontSize = 20.sp)
                    }

                    // Button to increase value
                    IconButton(
                        onClick = {
                            selectedHourBedTime++
                            selectedHourAwakeTime++
                                  },
                        modifier = Modifier
                            .background(Color.LightGray)
                            .fillMaxWidth()
                            .height(27.dp)
                            .weight(1f)
                    ) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase")
                    }
                }
            }

            // minutes
            Column (
                modifier = Modifier
                    .background(Color.Green, RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp))
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    IconButton(
                        onClick = {
                            selectedMinBedTime = (selectedMinBedTime - 10 + 60) % 60
                            selectedMinAwakeTime = (selectedMinAwakeTime - 10 + 60) % 60
                                  },
                        modifier = Modifier
                            .background(Color.LightGray)
                            .fillMaxWidth()
                            .height(27.dp)
                            .weight(1f)
                    ) {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease")
                    }

                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = formatMinute((selectedMinBedTime - 20 + 60) % 60), Modifier.alpha(0.2f), fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatMinute((selectedMinBedTime - 10 + 60) % 60), Modifier.alpha(0.5f), fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatMinute(selectedMinBedTime), Modifier.background(Color.LightGray), fontSize = 20.sp) //selected
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatMinute((selectedMinBedTime + 10) % 60), Modifier.alpha(0.5f), fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatMinute((selectedMinBedTime + 20) % 60), Modifier.alpha(0.2f), fontSize = 20.sp)
                    }

                    // Button to increase value
                    IconButton(
                        onClick = {
                            selectedMinBedTime = (selectedMinBedTime + 10) % 60
                            selectedMinAwakeTime = (selectedMinAwakeTime + 10) % 60
                                  },
                        modifier = Modifier
                            .background(Color.LightGray)
                            .fillMaxWidth()
                            .height(27.dp)
                            .weight(1f)
                    ) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // awake time

        Text(text = "Awake Time",
            Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp))

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.Transparent)
        ){
            //hours
            Column (
                modifier = Modifier
                    .background(
                        Color.Yellow,
                        RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                    )
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    IconButton(
                        onClick = {
                            selectedHourAwakeTime--
                            selectedHourBedTime--
                                  },
                        modifier = Modifier
                            .background(Color.LightGray)
                            .fillMaxWidth()
                            .height(27.dp)
                            .weight(1f)
                    ) {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease")
                    }

                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(text = formatHour(selectedHourAwakeTime - 2), Modifier.alpha(0.2f), fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatHour(selectedHourAwakeTime - 1), Modifier.alpha(0.5f), fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatHour(selectedHourAwakeTime), Modifier.background(Color.LightGray), fontSize = 20.sp) //selected
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatHour(selectedHourAwakeTime + 1), Modifier.alpha(0.5f), fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatHour(selectedHourAwakeTime + 2), Modifier.alpha(0.2f), fontSize = 20.sp)
                    }

                    // Button to increase value
                    IconButton(
                        onClick = {
                            selectedHourAwakeTime++
                            selectedHourBedTime++
                                  },
                        modifier = Modifier
                            .background(Color.LightGray)
                            .fillMaxWidth()
                            .height(27.dp)
                            .weight(1f)
                    ) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase")
                    }
                }
            }

            // minutes
            Column (
                modifier = Modifier
                    .background(Color.Green, RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp))
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    IconButton(
                        onClick = {
                            selectedMinAwakeTime = (selectedMinAwakeTime - 10 + 60) % 60
                            selectedMinBedTime = (selectedMinBedTime - 10 + 60) % 60
                                  },
                        modifier = Modifier
                            .background(Color.LightGray)
                            .fillMaxWidth()
                            .height(27.dp)
                            .weight(1f)
                    ) {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease")
                    }

                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = formatMinute((selectedMinAwakeTime - 20 + 60) % 60), Modifier.alpha(0.2f), fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatMinute((selectedMinAwakeTime - 10 + 60) % 60), Modifier.alpha(0.5f), fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatMinute(selectedMinAwakeTime), Modifier.background(Color.LightGray), fontSize = 20.sp) //selected
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatMinute((selectedMinAwakeTime + 10) % 60), Modifier.alpha(0.5f), fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatMinute((selectedMinAwakeTime + 20) % 60), Modifier.alpha(0.2f), fontSize = 20.sp)
                    }

                    // Button to increase value
                    IconButton(
                        onClick = {
                            selectedMinAwakeTime = (selectedMinAwakeTime + 10) % 60
                            selectedMinBedTime = (selectedMinBedTime + 10) % 60
                                  },
                        modifier = Modifier
                            .background(Color.LightGray)
                            .fillMaxWidth()
                            .height(27.dp)
                            .weight(1f)
                    ) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase")
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { showDialog = true }) {
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
                        Text("OK")
                    }
                }
            )
        }
    }
}

private fun getHourFromTarget(target: String) : Int {
    val decimalIndex = target.indexOf('h')
    if (decimalIndex != -1) {
        return target.substring(0, decimalIndex).toInt()
    }
    return 0
}
private fun getMinuteFromTarget(target: String) : Int {
    val decimalIndex = target.indexOf('h')
    if (decimalIndex != -1) {
        return target.substring(decimalIndex + 1, target.length).toInt()
    }
    return 0
}

// Function to format hours
private fun formatHour(hour: Int): String {
    val formattedHour = (hour % 24 + 24) % 24
    if (formattedHour < 10) {
        return "0$formattedHour"
    }
    return "$formattedHour"
}

// Function to format minutes
private fun formatMinute(minute: Int): String {
    val formattedMinute = (minute % 60 + 60) % 60
    if (formattedMinute < 10) {
        return "0$formattedMinute"
    }
    return "$formattedMinute"
}

private fun formatTarget(targetHours: Int, targetMinutes: Int) : Int {
    return (targetHours * 2) + (if (targetMinutes == 30) 1 else 0)
}