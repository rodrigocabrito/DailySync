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
import androidx.compose.runtime.mutableDoubleStateOf
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
fun EditSleepSchedule(navController: NavController, auth: FirebaseAuth, targetShow: Double = 8.5) {

    val target by remember { mutableDoubleStateOf(targetShow) }

    var selectedHourBedTime by remember { mutableIntStateOf(0) }
    var selectedMinBedTime by remember { mutableIntStateOf(0) }

    var showDialogSave by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val targetMinutes = if (hasDecimalPartFive(target)) 30 else 0

    var selectedHourAwakeTime by remember { mutableIntStateOf((selectedHourBedTime + target).toInt()) }
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
        navController.navigate(Screens.DefineSleepSchedule.route
            .replace(
                oldValue = "{bedTime}",
                newValue = "$selectedHourBedTime:$selectedMinBedTime"
            )
            .replace(
                oldValue = "{awakeTime}",
                newValue = "$selectedHourAwakeTime:$selectedMinAwakeTime"
            ))
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
                        onClick = { selectedMinAwakeTime = (selectedMinAwakeTime - 10 + 60) % 60 },
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
                        onClick = { selectedMinAwakeTime = (selectedMinAwakeTime + 10) % 60 },
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
                        Text("Ok")
                    }
                }
            )
        }
    }
}

// check if target has half hours
private fun hasDecimalPartFive(value: Double): Boolean {
    val stringValue = value.toString()
    val decimalIndex = stringValue.indexOf('.')

    if (decimalIndex != -1 && decimalIndex < stringValue.length - 1) {
        val decimalPart = stringValue.substring(decimalIndex + 1)
        return decimalPart == "5"
    }
    return false
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