package com.example.dailysync.home.exercise

import android.os.Bundle
import android.text.format.DateUtils.formatElapsedTime
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.dailysync.R
import com.example.dailysync.navigation.Screens
import com.google.android.gms.maps.MapView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DuringExercise(navController: NavController, categoryShow: Int, auth: FirebaseAuth) {

    var mapView: MapView? by remember { mutableStateOf(null) }
    val category by remember { mutableIntStateOf(categoryShow) }
    var elapsedTime by remember { mutableLongStateOf(0L) }
    var isChronometerRunning by remember { mutableStateOf(true) }
    var averagePace by remember { mutableFloatStateOf(0.0f) }
    var distance by remember { mutableFloatStateOf(0.0f) }          // TODO GET DISTANCE FROM GPS LOCATION

    Chronometer(isRunning = isChronometerRunning) { elapsedMillis ->
        elapsedTime = elapsedMillis
        averagePace = calculateAveragePace(distance, elapsedTime)
    }

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
            )
            .replace(
                oldValue = "{time}",
                newValue = "$elapsedTime"
            )
            .replace(
                oldValue = "{averagePace}",
                newValue = "$averagePace"
            )
            .replace(
                oldValue = "{distance}",
                newValue = "$distance"
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
        AndroidView(
            factory = { context ->
                MapView(context).apply {
                    mapView = this
                    this.onCreate(Bundle())
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(330.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .weight(3f)
                .width(350.dp)
                .padding(start = 20.dp, end = 20.dp, bottom = 15.dp)
                .background(Color(android.graphics.Color.parseColor("#A2F0C1")), shape = RoundedCornerShape(8.dp))
                .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text("Time: ${formatTime(elapsedTime)}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Average Pace: ${formatAveragePace(averagePace)}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Distance: $distance")                           // TODO LIVE DISTANCE
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            val buttonName = if (isChronometerRunning) "Pause" else "Resume"
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(50.dp)
                    .background(
                        Color(android.graphics.Color.parseColor("#47E285")),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        isChronometerRunning = !isChronometerRunning
                    }
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            ) {
                Text(
                    buttonName,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxHeight()
                        .fillMaxWidth()
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
                        showDialog = true
                    }
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            ) {
                Text(
                    "Finish",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxHeight()
                        .wrapContentSize(Alignment.Center)
                )
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

@Composable
private fun Chronometer(
    isRunning: Boolean,
    onTick: (Long) -> Unit
) {
    var elapsedTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            // Flow to emit elapsed time every second
            val timeFlow: Flow<Long> = flow {
                while (true) {
                    delay(1000)
                    elapsedTime += 1000
                    emit(elapsedTime)
                }
            }

            // Collect the elapsed time from the flow and update the UI
            launch {
                timeFlow.collect { elapsedMillis ->
                    onTick(elapsedMillis)
                }
            }
        }
    }
}

private fun formatTime(elapsedTime: Long): String {
    val totalSeconds = elapsedTime / 1000
    val hours = totalSeconds / 3600
    val remainingSeconds = totalSeconds % 3600
    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

// Function to calculate average pace
private fun calculateAveragePace(distance: Float, elapsedTime: Long): Float {
    // Convert elapsed time to minutes
    val elapsedMinutes = elapsedTime / 1000f / 60f
    // Calculate average pace in km/min
    return if (elapsedMinutes > 0) distance / elapsedMinutes else 0.0f
}

private fun formatAveragePace(averagePace: Float): String {
    return String.format(Locale.getDefault(), "%.1f km/min", averagePace)
}
