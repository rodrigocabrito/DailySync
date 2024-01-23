package com.example.dailysync.home.exercise

import android.Manifest
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.navigation.NavController
import com.example.dailysync.navigation.Screens
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth


@Composable
fun StartExercise(navController: NavController, categoryShow: Int, auth: FirebaseAuth) {

    val category by remember { mutableIntStateOf(categoryShow) }
    var mapView: MapView? by remember { mutableStateOf(null) }
    var currentLatLng by remember { mutableStateOf(LatLng(0.0, 0.0)) }

    val context = LocalContext.current
    val fusedLocationClient = remember(context) {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val title = when (category) {
        1 -> "Walk"
        2 -> "Run"
        else -> "Cycle"
    }

    var showDialog by remember { mutableStateOf(false) }

    // Press Yes (Pop Up)
    val confirmAction: () -> Unit = {
        navController.navigate(Screens.DuringExercise.route
            .replace(
                oldValue = "{category}",
                newValue = categoryShow.toString()
            ))
        showDialog = false
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

                        mapView?.getMapAsync { googleMap ->

                            moveToCurrentLocation(googleMap, currentLatLng)

                            // Enable blue dot on "My location"
                            googleMap.isMyLocationEnabled = true

                            val greenCircleBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(getGreenCircleBitmap())
                            val markerOptions = MarkerOptions()
                                .position(currentLatLng)
                                .icon(greenCircleBitmapDescriptor)
                                .title("My Location")

                            // Remove previous marker if any
                            googleMap.clear()

                            // Add the marker to the map
                            googleMap.addMarker(markerOptions)

                            googleMap.setOnMyLocationButtonClickListener {
                                // Move the camera to the current location when the button is clicked
                                moveToCurrentLocation(googleMap, currentLatLng)
                                true
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Location", "Error getting location", e)
                }
            // Set up location updates
            val locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000) // Update every 1 second

            val greenCircleBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(getGreenCircleBitmap())

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let { location ->
                        currentLatLng = LatLng(location.latitude, location.longitude)

                        mapView?.getMapAsync { googleMap ->
                            // Update the marker position
                            googleMap.clear()

                            val markerOptions = MarkerOptions()
                                .position(currentLatLng)
                                .icon(greenCircleBitmapDescriptor)
                                .title("My Location")

                            googleMap.addMarker(markerOptions)


                        }
                    }
                }
            }

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())

            // Dispose when the composable is removed from the hierarchy
            onDispose {
                // Stop location updates when the composable is disposed
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(
                context as ComponentActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }

        // Dispose when the composable is removed from the hierarchy
        onDispose {}
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
                .padding(start = 10.dp, end = 16.dp, top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp) // Adjust the spacing as needed
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color(0xFF0A361C)
                )
            ) { Icon(Icons.Default.ArrowBack, "Back") }

            IconButton(
                onClick = {
                    navController.navigate(Screens.Home.route)
                },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color(0xFF0A361C)
                )
            ) {
                Icon(Icons.Default.Home, "Home")
            }
        }

        // body

        Text(text = title, fontSize = 30.sp, color = Color(0xFF0A361C))

        Spacer(modifier = Modifier.height(26.dp))

        // Initialize Google Map when the view is first composed
        AndroidView(
            factory = { context ->
                MapView(context).apply {
                    mapView = this
                    this.onCreate(Bundle())
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(530.dp)
                .padding(start = 16.dp, end = 16.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(2.dp, Color(0xFF1A8B47), shape = RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.height(35.dp))

        Box(
            modifier = Modifier
                .width(100.dp)
                .height(50.dp)
                .background(
                    Color(android.graphics.Color.parseColor("#47E285")),
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable {
                    showDialog = true
                }
                .border(2.dp, Color(0xFF1A8B47), shape = RoundedCornerShape(8.dp))
        ) {
            Text(
                "Start",
                color = Color(0xFF0A361C),
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxHeight()
                    .wrapContentSize(Alignment.Center)
            )
        }

        if (showDialog) {
            AlertDialog(
                modifier = Modifier.border(2.dp, Color(0xFF1A8B47), shape = RoundedCornerShape(25.dp)),
                containerColor = Color(0xFFA2F0C1),
                onDismissRequest = {
                    showDialog = false
                },
                text = { Text("Are you ready to start your ${title}?", color =Color(0xFF0A361C), fontWeight = FontWeight.Bold) },
                confirmButton = {
                    TextButton(onClick = confirmAction) {
                        Text("Yes", color = Color(0xFF0A361C))
                    }
                },
                dismissButton = {
                    TextButton(onClick = cancelAction) {
                        Text("Cancel", color = Color(0xFF0A361C))
                    }
                }
            )
        }

    }
}

private fun moveToCurrentLocation(googleMap: GoogleMap, currentLatLng: LatLng) {
    googleMap.animateCamera(
        CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f)
    )
}

fun getGreenCircleBitmap(): Bitmap {
    val diameter = 55
    val bitmap = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val outerPaint = Paint().apply {
        color = Color(0xFFCCCCCC).toArgb()
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    val innerPaint = Paint().apply {
        color = Color(0xFF15723B).toArgb()
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    val radius = diameter / 2f

    // Draw outer circle (white border)
    canvas.drawCircle(radius, radius, radius, outerPaint)

    // Draw inner circle (green fill)
    canvas.drawCircle(radius, radius, radius - 8, innerPaint) // Adjust 2 to set the width of the white border

    return bitmap
}
