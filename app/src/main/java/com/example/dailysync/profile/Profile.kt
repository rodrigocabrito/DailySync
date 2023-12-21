package com.example.dailysync.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.dailysync.R
import com.example.dailysync.User
import com.example.dailysync.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(navController: NavController, auth: FirebaseAuth) {

    var showNameChange by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(auth.currentUser?.displayName)}
    val context = LocalContext.current

    val storage = FirebaseStorage.getInstance()
    val storageRef: StorageReference = storage.reference.child("profile_images")


    val imageUrlState = remember { mutableStateOf<String?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                uploadImageToFirebaseStorage(auth.currentUser?.uid, uri, storageRef)
                imageUrlState.value = uri.toString()
            }
        }
    }

    LaunchedEffect(auth.currentUser?.uid) {
        auth.currentUser?.uid?.let { userId ->
            try {
                val uri = storageRef.child("$userId.jpg").downloadUrl.await()
                imageUrlState.value = uri.toString()
            } catch (e: Exception) {
                // Handle the failure case, e.g., show an error message
                Toast.makeText(context, "Failed to get image URL: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
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
            // Align text to the right
            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.notification_icon),  // Replace R.drawable.ic_notification with your actual notification icon
                contentDescription = "Notification Icon",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        // Handle click on the notification icon
                        // TODO: Add your notification icon click logic
                    }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // body
        Image(
            painter = imageUrlState.value?.let { rememberImagePainter(data = it) }
                ?: rememberImagePainter(data = R.drawable.default_profile),
            contentScale = ContentScale.Crop,
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)

                .clickable {
                    openGallery(launcher)
                }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .clickable {
                    openGallery(launcher)
                }
                .border(1.dp, Color.Black, shape = RoundedCornerShape(15.dp))
                .align(Alignment.CenterHorizontally)
        ) { Text(text = " Change Profile picture ", fontSize = 15.sp)}
        Spacer(modifier = Modifier.height(20.dp))


        //name
        if (!showNameChange) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            {
                auth.currentUser?.displayName?.let { Text(text = it, fontSize = 30.sp) }
                Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    painter = painterResource(id = R.drawable.pencil_icon),
                    contentDescription = "Edit Icon",
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterVertically)
                        .clickable {
                            showNameChange = true
                        }
                )
            }
        }

        if (showNameChange) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            {
                name?.let {
                    TextField(
                        value = it,
                        onValueChange = { name = it },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.DarkGray,
                            containerColor = Color.White,
                            cursorColor = Color(0xFF0455BF),
                            focusedIndicatorColor = Color(0xFF0455BF),
                            unfocusedIndicatorColor = Color(0xFF0455BF)
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    painter = painterResource(id = R.drawable.complete_icon),
                    contentDescription = "Edit Icon",
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterVertically)
                        .clickable {
                            auth.currentUser?.updateProfile(
                                UserProfileChangeRequest.Builder().setDisplayName(name).build()

                            )?.addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    showNameChange = false
                                } else {
                                    // Name Update failed
                                    // Handle the error, log, or notify the user
                                }
                            }

                        }
                )
            }
        }

        //TODO Add change email and password

        // TODO OPTIONS (SETTINGS, ACHIEVEMENTS, ...)

        Spacer(modifier = Modifier.weight(1f))

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
                    .background(Color(android.graphics.Color.parseColor("#2C8CBC")))
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

private fun openGallery(pickImage: ActivityResultLauncher<Intent>) {
    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    pickImage.launch(intent)
}

private fun uploadImageToFirebaseStorage(userId: String?, imageUri: Uri, storageRef: StorageReference) {
    if (userId != null) {
        val userImageRef = storageRef.child("$userId.jpg")

        // Upload file to Firebase Storage
        userImageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Image uploaded successfully, you can get the download URL if needed
                val downloadUrl = taskSnapshot.storage.downloadUrl
                // TODO: Handle the download URL as needed (e.g., update user profile)
            }

    }
}