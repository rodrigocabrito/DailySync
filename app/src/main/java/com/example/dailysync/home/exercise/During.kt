package com.example.dailysync.home.exercise

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.text.format.DateUtils.formatElapsedTime
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.navigation.NavController
import com.example.dailysync.R
import com.example.dailysync.navigation.Screens
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
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

    var polylineOptions by remember { mutableStateOf(PolylineOptions()
        .color(Color.Blue.toArgb())         // Set the color to blue
        .width(10f)                         // Set the width of the polyline
    ) }
    var totalDistanceInMeters by remember { mutableFloatStateOf(0f) }
    var lastLocation: Location? by remember { mutableStateOf(null) }

    var distance by remember { mutableFloatStateOf((totalDistanceInMeters/1000)) }    // in km // TODO GET DISTANCE FROM GPS LOCATION

    var currentLatLng by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    val context = LocalContext.current
    val fusedLocationClient = remember(context) {
        LocationServices.getFusedLocationProviderClient(context)
    }

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

    DisposableEffect(context) {
        // Request location permission if not granted
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PermissionChecker.PERMISSION_GRANTED
        ) {
            // Request current location
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        currentLatLng = LatLng(it.latitude, it.longitude)

                        // Log the current coordinates
                        Log.e("Location", "Latitude: ${it.latitude}, Longitude: ${it.longitude}")

                        mapView?.getMapAsync { googleMap ->
                            // Move camera to the current location
                            googleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f)
                            )

                            // Enable blue dot on "My location"
                            googleMap.isMyLocationEnabled = true

                            // Update Polyline to draw the path
                            polylineOptions.add(currentLatLng)
                            googleMap.addPolyline(polylineOptions)

                            // Calculate distance
                            lastLocation?.let { last ->
                                val distance2 = last.distanceTo(it)
                                totalDistanceInMeters += distance2
                                Log.e("Distance", "Total Distance: $totalDistanceInMeters meters")
                            }

                            lastLocation = it
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Location", "Error getting location", e)
                }
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(
                context as ComponentActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }

        onDispose {}
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
                .padding(start = 10.dp, end = 16.dp, top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.Black
                )
            ) { Icon(Icons.Default.ArrowBack, "Back") }

            IconButton(
                onClick = {
                    navController.navigate(Screens.Notifications.route)
                },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.Black
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.notification_icon),
                    contentDescription = "Notifications",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // body

        Text(text = title, fontSize = 30.sp)

        Spacer(modifier = Modifier.height(26.dp))

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
                .padding(start = 16.dp, end = 16.dp)
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
                    fontSize = 20.sp,
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
                    fontSize = 20.sp,
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

        Spacer(modifier = Modifier.height(40.dp))
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
