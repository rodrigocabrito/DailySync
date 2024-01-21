package com.example.dailysync.home.exercise

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.dailysync.Exercise
import com.example.dailysync.R
import com.example.dailysync.navigation.Screens
import com.google.android.gms.maps.MapView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.time.Instant
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveExercise(navController: NavController, categoryShow: Int, auth: FirebaseAuth, timeShow: Long, averagePaceShow: Float, distanceShow: Float) {

    var mapView: MapView? by remember { mutableStateOf(null) }

    val category by remember { mutableIntStateOf(categoryShow) }
    val time by remember { mutableLongStateOf (timeShow) }
    val averagePace by remember { mutableFloatStateOf (averagePaceShow) }
    val distance by remember { mutableFloatStateOf (distanceShow) }

    var workoutName by remember { mutableStateOf("") }
    var workoutDescription by remember { mutableStateOf("") }

    var isCameraPreviewVisible by remember { mutableStateOf(false) }
    var capturedImageProxy: ImageProxy? by remember { mutableStateOf(null) }
    var isPictureTaken by remember { mutableStateOf(false) }
    var imageUri: Uri? by remember { mutableStateOf(null)}

    val userId = auth.currentUser?.uid

    val title = when (category) {
        1 -> "Walk"
        2 -> "Run"
        else -> "Cycle"
    }

    var showDialogSave by remember { mutableStateOf(false) }
    var showDialogCancel by remember { mutableStateOf(false) }
    var showDialogCancelConfirmed by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                imageUri = uri
            }
        }
    }

    // Press OK (Pop Up)
    val confirmAction: () -> Unit = {
        showDialogSave = false
        showDialogCancelConfirmed = false

        val exercise = Exercise(workoutName, workoutDescription, time, averagePace, distance, isPictureTaken, Instant.now().toEpochMilli())
        if (userId != null) {
            val exerciseId = writeToDatabase(userId, exercise, title)

            if (isPictureTaken) {
                imageUri?.let { uploadImageToFirebaseStorage(exerciseId, it) }
            }
        }
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

    // Open camera preview
    if (isCameraPreviewVisible) {
        CameraPreview(
            modifier = Modifier.fillMaxSize(),
            onPhotoCaptured = { imageProxy ->
                capturedImageProxy = imageProxy
                isCameraPreviewVisible = false
                isPictureTaken = true
            }
        )
    }

    // Handle the captured image
    capturedImageProxy?.let { imageProxy ->
        // Process the captured image (you can save it or display it)
        // For example, you can use the captured image in an ImageView
        // val bitmap = imageProxy.toBitmap()
        // imageView.setImageBitmap(bitmap)

        // Close the imageProxy when done
        imageProxy.close()
        capturedImageProxy = null

        // Reset the flag after handling the image
        isPictureTaken = false
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
            horizontalArrangement = Arrangement.End
        ) {
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

        Text(text = "Save $title", fontSize = 20.sp)

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = workoutName,
            onValueChange = {
                workoutName = it
            },

            label = { Text("Give a name to your $title", color = Color.DarkGray) },
            colors = TextFieldDefaults.textFieldColors(
                //textColor = Color.Black,
                containerColor = Color(android.graphics.Color.parseColor("#A2F0C1")),
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Black
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),

            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 5.dp)
        )

        TextField(
            value = workoutDescription,
            onValueChange = {
                workoutDescription = it
            },
            label = { Text("How did it go? Share some details...", color = Color.DarkGray)},
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            colors = TextFieldDefaults.textFieldColors(
                //textColor = Color.Black,
                containerColor = Color(android.graphics.Color.parseColor("#A2F0C1")),
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
        )

        Spacer(modifier = Modifier.height(12.dp))

        AndroidView(
            factory = { context ->
                MapView(context).apply {
                    mapView = this
                    this.onCreate(Bundle())
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(275.dp)
                .padding(start = 16.dp, end = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Box(
                modifier = Modifier
                    .weight(3f)
                    .height(110.dp)
                    .padding(start = 16.dp)
                    .background(
                        Color(android.graphics.Color.parseColor("#A2F0C1")),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                // TODO: FORMAT DISTANCE
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(start = 8.dp)
                ){
                    Text("Time: ${formatTime(time)}")
                    Text("Average Pace: ${formatAveragePace(averagePace)}")
                    Text("Distance: $distance")
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
                            Color(android.graphics.Color.parseColor("#A2F0C1"))
                        )
                        .clickable {
                            // TODO ACCESS PHONE GALLERY
                            openGallery(launcher)
                        }
                        .drawBehind {
                            drawRoundRect(
                                color = Color.Black, style = Stroke(
                                    width = 2f,
                                    pathEffect = PathEffect.dashPathEffect(
                                        floatArrayOf(10f, 10f),
                                        0f
                                    )
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
                            isCameraPreviewVisible = true
                        }
                        .drawBehind {
                            drawRoundRect(
                                color = Color.Black, style = Stroke(
                                    width = 2f,
                                    pathEffect = PathEffect.dashPathEffect(
                                        floatArrayOf(10f, 10f),
                                        0f
                                    )
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

        Spacer(modifier = Modifier.height(10.dp))

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
                    fontSize = 20.sp,
                    modifier = Modifier
                        .align(Alignment.Center)
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
                    fontSize = 20.sp,
                    modifier = Modifier
                        .align(Alignment.Center)
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
                        Text("OK")
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
                        Text("OK")
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
                        .padding(top = 10.dp),
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

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    onPhotoCaptured: (ImageProxy) -> Unit
) {
    val context = LocalContext.current
    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }

    // Use MainThreadExecutor to execute UI operations on the main thread
    val mainThreadExecutor = LocalContext.current.mainExecutor

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

            cameraProviderFuture.addListener({
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                mainThreadExecutor.execute {
                    val preview = Preview.Builder()
                        .build()
                        .also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            (context as androidx.activity.ComponentActivity),
                            cameraSelector,
                            preview
                        )

                        val imageAnalysis = ImageAnalysis.Builder()
                            .build()
                            .also {
                                it.setAnalyzer(cameraExecutor) { imageProxy ->
                                    // Do something with the captured image
                                    onPhotoCaptured.invoke(imageProxy)
                                    imageProxy.close()
                                }
                            }

                        cameraProvider.bindToLifecycle(
                            (context as androidx.activity.ComponentActivity),
                            cameraSelector,
                            preview,
                            imageAnalysis
                        )

                    } catch (exc: Exception) {
                        // Handle errors
                    }
                }
            }, cameraExecutor)

            previewView
        },
    )
}


private fun formatTime(elapsedTime: Long): String {
    val totalSeconds = elapsedTime / 1000
    val hours = totalSeconds / 3600
    val remainingSeconds = totalSeconds % 3600
    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

private fun formatAveragePace(averagePace: Float): String {
    return String.format(Locale.getDefault(), "%.1f km/min", averagePace)
}

private fun openGallery(pickImage: ActivityResultLauncher<Intent>) {
    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    pickImage.launch(intent)
}

private fun writeToDatabase(userId: String, exercise: Exercise, category: String) : String {
    val database = Firebase.database
    val exerciseRef = database.getReference("users").child(userId).child(category)

    // Create a new unique ID for the workout
    val workoutId = exerciseRef.push().key

    exerciseRef.child(workoutId!!).setValue(exercise)

    return workoutId
}

private fun uploadImageToFirebaseStorage(exerciseId: String, imageUri: Uri) {
    val storage = FirebaseStorage.getInstance()
    val storageRef: StorageReference = storage.reference
    val imageRef: StorageReference = storageRef.child("exercise_images/$exerciseId.jpg")

    // Upload file to Firebase Storage
    imageRef.putFile(imageUri)
        .addOnSuccessListener { taskSnapshot ->
            // Image uploaded successfully, you can get the download URL if needed
            val downloadUrl = taskSnapshot.storage.downloadUrl
            // TODO: Handle the download URL as needed (e.g., update user profile)
        }
}

fun Bitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 100, stream)
    return stream.toByteArray()
}