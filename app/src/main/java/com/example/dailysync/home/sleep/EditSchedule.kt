package com.example.dailysync.home.sleep

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dailysync.SleepTarget
import com.example.dailysync.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

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

    val userId = auth.currentUser?.uid


    fun calculateHour(selectedHourBed: Boolean, selectedMinBed: Boolean, selectedHourAwake: Boolean, selectedMinAwake: Boolean) {
        if (selectedHourBed) {  // selectedHourBedTime changed
            val updatedAwakeTime = updateHourAndMinute(selectedHourBedTime, selectedMinBedTime, targetHours, targetMinutes)
            selectedHourAwakeTime = updatedAwakeTime.first
            selectedMinAwakeTime = updatedAwakeTime.second

        } else if (selectedMinBed) {   // selectedMinBedTime changed
            val updatedAwakeTime = updateHourAndMinute(selectedHourBedTime, selectedMinBedTime, targetHours, targetMinutes)
            selectedHourAwakeTime = updatedAwakeTime.first
            selectedMinAwakeTime = updatedAwakeTime.second

        } else if (selectedHourAwake) {   // selectedHourAwakeTime changed
            val updatedBedTime = calculateInitialTime(selectedHourAwakeTime, selectedMinAwakeTime, targetHours, targetMinutes)
            selectedHourBedTime = updatedBedTime.first
            selectedMinBedTime = updatedBedTime.second

        } else if (selectedMinAwake) {   // selectedMinAwakeTime changed
            val updatedBedTime = calculateInitialTime(selectedHourAwakeTime, selectedMinAwakeTime, targetHours, targetMinutes)
            selectedHourBedTime = updatedBedTime.first
            selectedMinBedTime = updatedBedTime.second
        }
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

        val targetToPassBack = formatTarget(targetHours, targetMinutes)

        val minBedTime = if (selectedMinBedTime == 0) {
            "00"
        } else  if (selectedMinBedTime < 0) {
            (60 + selectedMinBedTime).toString()
        } else {
            selectedMinBedTime.toString()
        }

        val minAwakeTime = if (selectedMinAwakeTime == 0) {
            "00"
        } else  if (selectedMinAwakeTime < 0) {
            (60 + selectedMinAwakeTime).toString()
        }else {
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

        // saving target and schedule in db
        val target = if (targetMinutes == 30) (targetHours*2) + 1 else targetHours*2
        val sleepTarget = SleepTarget(hourBedTime.toInt(), minBedTime.toInt(), hourAwakeTime.toInt(), minAwakeTime.toInt(), target)
        if (userId != null) {
            writeToDatabase(userId, sleepTarget)
        }

        navController.navigate(Screens.DefineSleepSchedule.route.replace(
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
                .padding(start = 10.dp, end = 16.dp, top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color(0xF1301E58)
                )
            ) { Icon(Icons.Default.ArrowBack, "Back") }
            IconButton(
                onClick = {
                    navController.navigate(Screens.Home.route)
                },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color(0xF1301E58)
                )
            ) {
                Icon(Icons.Default.Home, "Home")
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        // body

        Spacer(modifier = Modifier.height(30.dp))

        Text(text = "Edit Sleep Schedule", fontSize = 30.sp, color = Color(0xF1301E58))

        Spacer(modifier = Modifier.height(36.dp))

        // bed time
        Box(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp)
                .width(150.dp)
                .background(
                    Color(android.graphics.Color.parseColor("#CCBCEE")),
                    shape = RoundedCornerShape(8.dp)
                )
                .border(2.dp, Color(0xF14B3283), RoundedCornerShape(8.dp)),
        ) {
            Text(
                "\uD83D\uDECF Bed Time",
                color = Color(0xF1301E58),
                modifier = Modifier
                    .align(Alignment.Center)
                    .height(35.dp)

                    .wrapContentSize(Alignment.Center)
            )
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.Transparent)
                .border(2.dp, Color(0xF14B3283), RoundedCornerShape(15.dp)),
        ){
            //hours
            Column (
                modifier = Modifier
                    .background(
                        Color(0xFFF3F3F3),
                        RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                    )
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    // Button to increase value
                    IconButton(
                        onClick = {
                            selectedHourBedTime++
                            calculateHour(true, false, false, false)
                        },
                        modifier = Modifier
                            .background(Color(0xFFF3F3F3))
                            .fillMaxWidth()
                            .height((29.5).dp)
                            .weight(1f)
                    ) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase", tint = Color(0xFF5931B0))
                    }

                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(text = formatHour(selectedHourBedTime + 2), Modifier.alpha(0.2f), fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatHour(selectedHourBedTime + 1), Modifier.alpha(0.5f), fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatHour(selectedHourBedTime) + "h", Modifier.background(Color(0xFFF3F3F3)), fontSize = 20.sp, color = Color(0xFF5931B0), fontWeight = FontWeight.Bold) //selected
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatHour(selectedHourBedTime - 1), Modifier.alpha(0.5f), fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatHour(selectedHourBedTime - 2), Modifier.alpha(0.2f), fontSize = 20.sp)
                    }


                    IconButton(
                        onClick = {
                            selectedHourBedTime--
                            calculateHour(true, false, false, false)
                        },
                        modifier = Modifier
                            .background(Color(0xFFF3F3F3))
                            .fillMaxWidth()
                            .height((29.5).dp)
                            .weight(1f)
                    ) {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease", tint = Color(0xFF5931B0))
                    }

                }
            }

            Divider(color = Color(0xF14B3283), modifier = Modifier.width(2.dp).height(168.dp))

            // minutes
            Column (
                modifier = Modifier
                    .background(Color(0xFFF3F3F3), RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp))
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    // Button to increase value
                    IconButton(
                        onClick = {
                            selectedMinBedTime = (selectedMinBedTime + 10) % 60
                            calculateHour(false, true, false, false)
                        },
                        modifier = Modifier
                            .background(Color(0xFFF3F3F3))
                            .fillMaxWidth()
                            .height((29.5).dp)
                            .weight(1f)
                    ) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase",tint = Color(0xFF5931B0))
                    }

                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = formatMinute((selectedMinBedTime + 20) % 60), Modifier.alpha(0.2f), fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatMinute((selectedMinBedTime + 10) % 60), Modifier.alpha(0.5f), fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatMinute(selectedMinBedTime) + "m", Modifier.background(Color(0xFFF3F3F3)), fontSize = 20.sp, color = Color(0xFF5931B0), fontWeight = FontWeight.Bold) //selected
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatMinute((selectedMinBedTime - 10 + 60) % 60), Modifier.alpha(0.5f), fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatMinute((selectedMinBedTime - 20 + 60) % 60), Modifier.alpha(0.2f), fontSize = 20.sp)
                    }


                    IconButton(
                        onClick = {
                            selectedMinBedTime = (selectedMinBedTime - 10 + 60) % 60
                            calculateHour(false, true, false, false)
                        },
                        modifier = Modifier
                            .background(Color(0xFFF3F3F3))
                            .fillMaxWidth()
                            .height((29.5).dp)
                            .weight(1f)
                    ) {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease", tint = Color(0xFF5931B0))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // awake time

        Box(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp)
                .width(150.dp)
                .background(
                    Color(android.graphics.Color.parseColor("#CCBCEE")),
                    shape = RoundedCornerShape(8.dp)
                )
                .border(2.dp, Color(0xF14B3283), RoundedCornerShape(8.dp)),
        ) {
            Text(
                "⏰ Awake Time",
                color = Color(0xF1301E58),
                modifier = Modifier
                    .align(Alignment.Center)
                    .height(35.dp)

                    .wrapContentSize(Alignment.Center)
            )
        }


        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.Transparent)
                .border(2.dp, Color(0xF14B3283), RoundedCornerShape(15.dp)),
        ){
            //hours
            Column (
                modifier = Modifier
                    .background(
                        Color(0xFFF3F3F3),
                        RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                    )
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    // Button to increase value
                    IconButton(
                        onClick = {
                            selectedHourAwakeTime++
                            calculateHour(false, false, true, false)
                        },
                        modifier = Modifier
                            .background(Color(0xFFF3F3F3))
                            .fillMaxWidth()
                            .height((29.5).dp)
                            .weight(1f)
                    ) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase", tint = Color(0xFF5931B0))
                    }

                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(text = formatHour(selectedHourAwakeTime + 2), Modifier.alpha(0.2f), fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatHour(selectedHourAwakeTime + 1), Modifier.alpha(0.5f), fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatHour(selectedHourAwakeTime) + "h", Modifier.background(Color(0xFFF3F3F3)), fontSize = 20.sp, color = Color(0xFF5931B0), fontWeight = FontWeight.Bold) //selected
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatHour(selectedHourAwakeTime - 1), Modifier.alpha(0.5f), fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatHour(selectedHourAwakeTime - 2), Modifier.alpha(0.2f), fontSize = 20.sp)
                    }


                    IconButton(
                        onClick = {
                            selectedHourAwakeTime--
                            calculateHour(false, false, true, false)
                        },
                        modifier = Modifier
                            .background(Color(0xFFF3F3F3))
                            .fillMaxWidth()
                            .height((29.5).dp)
                            .weight(1f)
                    ) {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease", tint = Color(0xFF5931B0))
                    }
                }
            }

            Divider(color = Color(0xF14B3283), modifier = Modifier.width(2.dp).height(168.dp))

            // minutes
            Column (
                modifier = Modifier
                    .background(Color(0xFFF3F3F3), RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp))
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                ){

                    // Button to increase value
                    IconButton(
                        onClick = {
                            selectedMinAwakeTime = (selectedMinAwakeTime + 10) % 60
                            calculateHour(false, false, false, true)
                        },
                        modifier = Modifier
                            .background(Color(0xFFF3F3F3))
                            .fillMaxWidth()
                            .height((29.5).dp)
                            .weight(1f)
                    ) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase", tint = Color(0xFF5931B0))
                    }

                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = formatMinute((selectedMinAwakeTime + 20 ) % 60), Modifier.alpha(0.2f), fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatMinute((selectedMinAwakeTime + 10 ) % 60), Modifier.alpha(0.5f), fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatMinute(selectedMinAwakeTime) + "m", Modifier.background(Color(0xFFF3F3F3)), fontSize = 20.sp, color = Color(0xFF5931B0), fontWeight = FontWeight.Bold) //selected
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatMinute((selectedMinAwakeTime - 10 + 60) % 60), Modifier.alpha(0.5f), fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatMinute((selectedMinAwakeTime - 20 + 60) % 60), Modifier.alpha(0.2f), fontSize = 20.sp)
                    }


                    IconButton(
                        onClick = {
                            selectedMinAwakeTime = (selectedMinAwakeTime - 10 + 60) % 60
                            calculateHour(false, false, false, true)
                        },
                        modifier = Modifier
                            .background(Color(0xFFF3F3F3))
                            .fillMaxWidth()
                            .height((29.5).dp)
                            .weight(1f)
                    ) {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease", tint = Color(0xFF5931B0))
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .width(110.dp)
                .height(50.dp)
                .background(
                    Color(android.graphics.Color.parseColor("#A17FEB")),
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable {
                    showDialog = true
                }
                .border(2.dp, Color(0xF14B3283), RoundedCornerShape(8.dp)),
        ) {
            Text(
                "Save",
                color = Color(0xF1301E58),
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxHeight()
                    .wrapContentSize(Alignment.Center)
            )
        }

        // Show the AlertDialog Pop Up
        if (showDialog) {
            AlertDialog(
                modifier = Modifier.border(2.dp, Color(0xF14B3283), shape = RoundedCornerShape(25.dp)),
                containerColor = Color(0xFFCCBCEE),
                onDismissRequest = {
                    // Handle dialog dismiss (e.g., when tapping outside the dialog)
                    showDialog = false
                },
                text = { Text("Do you want to save this sleep schedule?",color = Color(0xF1301E58)) },
                confirmButton = {
                    TextButton(onClick = confirmAction) {
                        Text("Yes",color = Color(0xF1301E58))
                    }
                },
                dismissButton = {
                    TextButton(onClick = cancelAction) {
                        Text("Cancel",color = Color(0xF1301E58))
                    }
                }
            )
        }

        // Show the AlertDialog Pop Up
        if (showDialogSave) {
            AlertDialog(
                modifier = Modifier.border(2.dp, Color(0xF14B3283), shape = RoundedCornerShape(25.dp)),
                containerColor = Color(0xFFCCBCEE),
                onDismissRequest = {
                    // Handle dialog dismiss (e.g., when tapping outside the dialog)
                    showDialogSave = false
                },
                text = { Text("Your sleep schedule was saved successfully!",color = Color(0xF1301E58)) },
                confirmButton = {
                    TextButton(onClick = confirmActionSaved) {
                        Text("OK",color = Color(0xF1301E58))
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

private fun updateHourAndMinute(hour1: Int, minute1: Int, hoursDifference: Int, minutesDifference: Int): Pair<Int, Int> {
    val totalMinutes1 = hour1 * 60 + minute1

    val totalMinutes2 = (totalMinutes1 + hoursDifference * 60 + minutesDifference) % (24 * 60)

    val updatedHour2 = totalMinutes2 / 60
    val updatedMinute2 = totalMinutes2 % 60

    return Pair(updatedHour2, updatedMinute2)
}

private fun calculateInitialTime(hour2: Int, minute2: Int, hoursDifference: Int, minutesDifference: Int): Pair<Int, Int> {
    val totalMinutes2 = hour2 * 60 + minute2

    val totalMinutes1 = (totalMinutes2 - hoursDifference * 60 - minutesDifference + 24 * 60) % (24 * 60)

    val initialHour1 = totalMinutes1 / 60
    val initialMinute1 = totalMinutes1 % 60

    return Pair(initialHour1, initialMinute1)
}

private fun writeToDatabase(userId: String, sleepTarget: SleepTarget) {
    val database = Firebase.database
    val sleepTargetRef = database.getReference("users").child(userId).child("SleepTarget")
    sleepTargetRef.setValue(sleepTarget)
}

