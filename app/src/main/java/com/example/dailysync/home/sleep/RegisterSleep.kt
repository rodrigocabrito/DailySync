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
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dailysync.R
import com.example.dailysync.Sleep
import com.example.dailysync.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.Instant

@Composable
fun RegisterSleep(navController: NavController, auth: FirebaseAuth) {

    var selectedHourBedTime by remember { mutableIntStateOf(0) }
    var selectedMinBedTime by remember { mutableIntStateOf(0) }
    var selectedHourAwakeTime by remember { mutableIntStateOf(0) }
    var selectedMinAwakeTime by remember { mutableIntStateOf(0) }

    var showDialogSave by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val userId = auth.currentUser?.uid

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

        val minBedTime = if (selectedMinBedTime < 0) {
            (60 + selectedMinBedTime)
        } else {
            selectedMinBedTime
        }

        val minAwakeTime = if (selectedMinAwakeTime < 0) {
            (60 + selectedMinAwakeTime)
        } else {
            selectedMinAwakeTime
        }

        val hourBedTime = if (selectedHourBedTime < 0) {
            (24 + selectedHourBedTime)
        } else {
            selectedHourBedTime
        }

        val hourAwakeTime = if (selectedHourAwakeTime < 0) {
            (24 + selectedHourAwakeTime)
        } else {
            selectedHourAwakeTime
        }

        val hourSlept = getHourDifference(hourBedTime, minBedTime, hourAwakeTime, minAwakeTime)
        val minSlept = getMinDifference(hourBedTime, minBedTime, hourAwakeTime, minAwakeTime)
        val sleep = Sleep(hourBedTime, minBedTime, hourAwakeTime, minAwakeTime, hourSlept, minSlept, Instant.now().toEpochMilli())

        if (userId != null) {
            writeToDatabase(userId, sleep)
        }
        navController.navigate(Screens.Home.route)
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
            FloatingActionButton(
                modifier = Modifier
                    .size(60.dp)
                    .border(2.dp, Color(0xF14B3283), RoundedCornerShape(15.dp)),
                onClick = {
                    navController.navigate(Screens.DefineSleepSchedule.route)
                },
                containerColor = Color(0xFFCCBCEE),
                contentColor = Color(0xF1301E58),
            ) {
                Icon(
                    modifier = Modifier.size(25.dp),
                    painter = painterResource(id = R.drawable.schedule),
                    contentDescription = "Start or register reading session",
                )

            }
        }

        Spacer(modifier = Modifier.height(30.dp))
        // body
        Text(text = "Register Sleep", fontSize = 30.sp, color = Color(0xF1301E58))

        Spacer(modifier = Modifier.height(26.dp))

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
                .border(2.dp, Color(0xF14B3283), shape = RoundedCornerShape(8.dp))
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
                .border(2.dp, Color(0xF14B3283), shape = RoundedCornerShape(8.dp))
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
                        Text(text = formatMinute(selectedMinBedTime) + "m", Modifier.background(
                            Color(0xFFF3F3F3)
                        ), fontSize = 20.sp, color = Color(0xFF5931B0), fontWeight = FontWeight.Bold) //selected
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatMinute((selectedMinBedTime - 10 + 60) % 60), Modifier.alpha(0.5f), fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatMinute((selectedMinBedTime - 20 + 60) % 60), Modifier.alpha(0.2f), fontSize = 20.sp)
                    }


                    IconButton(
                        onClick = {
                            selectedMinBedTime = (selectedMinBedTime - 10 + 60) % 60
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
                .border(2.dp, Color(0xF14B3283), shape = RoundedCornerShape(8.dp))
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
                .border(2.dp, Color(0xF14B3283), shape = RoundedCornerShape(8.dp))
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
                        Text(text = formatHour(selectedHourAwakeTime) + "h", Modifier.background(
                            Color(0xFFF3F3F3)
                        ), fontSize = 20.sp, color = Color(0xFF5931B0), fontWeight = FontWeight.Bold) //selected
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatHour(selectedHourAwakeTime - 1), Modifier.alpha(0.5f), fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatHour(selectedHourAwakeTime - 2), Modifier.alpha(0.2f), fontSize = 20.sp)
                    }


                    IconButton(
                        onClick = {
                            selectedHourAwakeTime--
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
                        Text(text = formatMinute(selectedMinAwakeTime) + "m", Modifier.background(
                            Color(0xFFF3F3F3)
                        ), fontSize = 20.sp, color = Color(0xFF5931B0), fontWeight = FontWeight.Bold) //selected
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatMinute((selectedMinAwakeTime - 10 + 60) % 60), Modifier.alpha(0.5f), fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = formatMinute((selectedMinAwakeTime - 20 + 60) % 60), Modifier.alpha(0.2f), fontSize = 20.sp)
                    }


                    IconButton(
                        onClick = {
                            selectedMinAwakeTime = (selectedMinAwakeTime - 10 + 60) % 60
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

        Spacer(modifier = Modifier.height(16.dp))

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
                .border(2.dp, Color(0xF14B3283), shape = RoundedCornerShape(8.dp))
        ) {
            Text(
                "Save",
                fontSize = 20.sp,
                color = Color(0xF1301E58),
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
                text = { Text("Do you want to save this Sleep?",color = Color(0xF1301E58)) },
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
                text = { Text("Your sleep was saved successfully!",color = Color(0xF1301E58)) },
                confirmButton = {
                    TextButton(onClick = confirmActionSaved) {
                        Text("OK",color = Color(0xF1301E58))
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
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

private fun writeToDatabase(userId: String, sleep: Sleep) {
    val database = Firebase.database
    val sleepRef = database.getReference("users").child(userId).child("Sleep")

    // Create a new unique ID for the workout
    val sleepId = sleepRef.push().key

    sleepRef.child(sleepId!!).setValue(sleep)
}

private fun getHourDifference(hour1: Int, minute1: Int, hour2: Int, minute2: Int): Int {
    val totalMinutes1 = hour1 * 60 + minute1
    val totalMinutes2 = hour2 * 60 + minute2

    val differenceMinutes = (totalMinutes2 - totalMinutes1 + 24 * 60) % (24 * 60)

    return differenceMinutes / 60
}

private fun getMinDifference(hour1: Int, minute1: Int, hour2: Int, minute2: Int): Int {
    val totalMinutes1 = hour1 * 60 + minute1
    val totalMinutes2 = hour2 * 60 + minute2

    val differenceMinutes = (totalMinutes2 - totalMinutes1 + 24 * 60) % (24 * 60)

    return differenceMinutes % 60
}
