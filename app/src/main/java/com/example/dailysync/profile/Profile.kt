package com.example.dailysync.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.dailysync.R
import com.example.dailysync.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

@Composable
fun Profile(navController: NavController, auth: FirebaseAuth) {

    var showNameChange by remember { mutableStateOf(false) }
    val name by remember { mutableStateOf(auth.currentUser?.displayName)}

    val storage = FirebaseStorage.getInstance()
    val storageRef: StorageReference = storage.reference.child("profile_images")

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val usersRef: DatabaseReference = database.getReference("users")


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
                painter = painterResource(id = R.drawable.notification_icon),
                contentDescription = "Notification Icon",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        //navController.navigate(Screens.Notifications.route)
                    }
            )
        }



        // body
        Image(
            painter = imageUrlState.value?.let { rememberAsyncImagePainter(model = it) }
                ?: rememberAsyncImagePainter(model = R.drawable.default_profile),
            contentScale = ContentScale.Crop,
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)

                .clickable {
                    openGallery(launcher)
                }
                .border(
                    width = 2.dp, // Adjust the width of the border as needed
                    color = Color(0xFFA2D6F0), // Adjust the color of the border as needed
                    shape = CircleShape
                )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .height(25.dp)
                .clickable {
                    openGallery(launcher)
                }
                .border(2.dp, Color(0xFFA2D6F0), shape = RoundedCornerShape(15.dp))
                .align(Alignment.CenterHorizontally)
        ) { Text(text = "  Change Profile picture  ", fontSize = 15.sp)}
        Spacer(modifier = Modifier.height(8.dp))


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
                    tint = Color(0xFF0455BF),
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
                    .fillMaxWidth()
            ) {
                name?.let {
                    var newName by remember { mutableStateOf(it) }

                    // TextField
                    TextField(
                        value = newName,
                        onValueChange = { newName = it },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.DarkGray,
                            unfocusedTextColor = Color.DarkGray,
                            focusedIndicatorColor = Color(0xFF0455BF),
                            unfocusedIndicatorColor = Color(0xFF0455BF),
                            cursorColor = Color(0xFF0455BF),
                            focusedLabelColor = Color.DarkGray,
                            unfocusedLabelColor = Color.DarkGray,
                            focusedContainerColor = Color(Color(0xFFBAE8EE).toArgb()),
                            unfocusedContainerColor = Color(Color(0xFFBAE8EE).toArgb())
                        ),
                        textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                // Update the name in the Realtime Database when Done action is performed
                                auth.currentUser?.uid?.let { userId ->
                                    usersRef.child(userId).child("name").setValue(newName)
                                }

                                // Dismiss the keyboard and update the profile
                                auth.currentUser?.updateProfile(
                                    UserProfileChangeRequest.Builder().setDisplayName(newName).build()
                                )?.addOnCompleteListener { updateTask ->
                                    if (updateTask.isSuccessful) {
                                        showNameChange = false
                                    } else {
                                        // Handle the error, log, or notify the user
                                    }
                                }
                            }
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    Spacer(modifier = Modifier.width((-2).dp))

                    // Confirm Icon Button
                    Icon(
                        painter = painterResource(id = R.drawable.complete_icon),
                        contentDescription = "Confirm Icon",
                        tint = Color(0xFF0455BF),
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterVertically)
                            .clickable {
                                // Similar logic as onDone to update the name
                                auth.currentUser?.uid?.let { userId ->
                                    usersRef.child(userId).child("name").setValue(newName)
                                }

                                auth.currentUser?.updateProfile(
                                    UserProfileChangeRequest.Builder().setDisplayName(newName).build()
                                )?.addOnCompleteListener { updateTask ->
                                    if (updateTask.isSuccessful) {
                                        showNameChange = false
                                    } else {
                                        // Handle the error, log, or notify the user
                                    }
                                }
                            }
                    )
                }
            }
        }

        Column{
            // SETTINGS, ACHIEVEMENTS
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,){
                //Settings
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                        .weight(1f)
                        .height(115.dp)
                        .background(
                            Color(android.graphics.Color.parseColor("#CBF4F7")),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            // does nothing
                        }
                        .border(2.dp, Color(0xFFA2D6F0), shape = RoundedCornerShape(8.dp))
                ) {
                        Text(text = "Settings",
                            fontSize = 30.sp,
                            modifier = Modifier.padding(start = 8.dp, top = 15.dp),

                            )
                        Icon(
                            painter = painterResource(id = R.drawable.settings_icon),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(50.dp)
                                .align(Alignment.BottomEnd)
                                .padding(bottom = 10.dp, end = 10.dp)

                        )

                }
                //Achievements
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                        .weight(1f)
                        .height(115.dp)
                        .background(
                            Color(android.graphics.Color.parseColor("#CBF4F7")),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            // does nothing
                        }
                        .border(2.dp, Color(0xFFA2D6F0), shape = RoundedCornerShape(8.dp))
                ) {
                        Text(text = "Change Email & Password",
                            fontSize = 22.sp,
                            modifier = Modifier.padding(start = 8.dp, top = 15.dp),

                            )
                        Icon(
                            painter = painterResource(id = R.drawable.change_icon),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(45.dp)
                                .align(Alignment.BottomEnd)
                                .padding(bottom = 10.dp, end = 10.dp)
                        )

                }
            }

            // faqs, contact us
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,){
                //Settings
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                        .weight(1f)
                        .height(115.dp)
                        .background(
                            Color(android.graphics.Color.parseColor("#CBF4F7")),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            // does nothing
                        }
                        .border(2.dp, Color(0xFFA2D6F0), shape = RoundedCornerShape(8.dp))
                ) {
                        Text(text = "FAQs",
                            fontSize = 30.sp,
                            modifier = Modifier.padding(start = 8.dp, top = 15.dp),

                            )
                        Icon(
                            painter = painterResource(id = R.drawable.faqs_icon),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(80.dp)
                                .align(Alignment.BottomEnd)
                                .padding(bottom = 10.dp, end = 10.dp)
                        )
                }
                //Achievements
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                        .weight(1f)
                        .height(115.dp)
                        .background(
                            Color(android.graphics.Color.parseColor("#CBF4F7")),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            // does nothing
                        }
                        .border(2.dp, Color(0xFFA2D6F0), shape = RoundedCornerShape(8.dp))
                ) {
                        Text(text = "Contact Us",
                            fontSize = 30.sp,
                            modifier = Modifier.padding(start = 8.dp, top = 15.dp),

                            )
                        Icon(
                            painter = painterResource(id = R.drawable.headset_icon),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(65.dp)
                                .align(Alignment.BottomEnd)
                                .padding(bottom = 10.dp, end = 10.dp)
                        )
                }
            }

            // Feedback, Rate the app
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,){
                //Feedback
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                        .weight(1f)
                        .height(115.dp)
                        .background(
                            Color(android.graphics.Color.parseColor("#CBF4F7")),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            // does nothing
                        }
                        .border(2.dp, Color(0xFFA2D6F0), shape = RoundedCornerShape(8.dp))
                ) {
                        Text(text = "Give FeedBack",
                            fontSize = 22.sp,
                            modifier = Modifier.padding(start = 8.dp, top = 15.dp),

                            )
                        Icon(
                            painter = painterResource(id = R.drawable.feedback_icon),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(45.dp)
                                .align(Alignment.BottomEnd)
                                .padding(bottom = 10.dp, end = 10.dp)
                        )
                }
                //Rate the app
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                        .weight(1f)
                        .height(115.dp)
                        .background(
                            Color(0xFFCBF4F7),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            auth.signOut()
                            navController.navigate(Screens.Login.route)
                        }
                        .border(2.dp, Color(0xFFA2D6F0), shape = RoundedCornerShape(8.dp))
                ) {
                        Text(text = "Logout",
                            fontSize = 30.sp,
                            modifier = Modifier.padding(start = 8.dp, top = 15.dp),

                            )
                        Icon(
                            painter = painterResource(id = R.drawable.logout),
                            contentDescription = null,
                            tint = Color(0xFF041342),
                            modifier = Modifier
                                .size(50.dp)
                                .align(Alignment.BottomEnd)
                                .padding(bottom = 10.dp, end = 10.dp)
                        )
                }
            }
        }


        Spacer(modifier = Modifier.weight(1f))
        NavigationBar(containerColor = Color(0xFFBCD7E4)
        ) {
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color(0xFF68ADD1)
                ),
                icon = { Icon(
                    painter = painterResource(id = R.drawable.home_icon),
                    contentDescription = null,
                    tint = Color(0xFF021D3F),
                    modifier = Modifier
                        .size(45.dp)
                ) },
                onClick = {navController.navigate(Screens.Home.route)},
                selected = false
            )
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color(0xFF68ADD1)
                ),
                icon = { Icon(
                    painter = painterResource(id = R.drawable.report_icon),
                    contentDescription = null,
                    tint = Color(0xFF021D3F),
                    modifier = Modifier
                        .size(40.dp)
                ) },
                onClick = {navController.navigate(Screens.Reports.route)},
                selected = false
            )
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color(0xFF68ADD1)
                ),
                icon = { Icon(
                    painter = painterResource(id = R.drawable.community_icon),
                    contentDescription = null,
                    tint = Color(0xFF021D3F),
                    modifier = Modifier
                        .size(55.dp)
                ) },
                onClick = {navController.navigate(Screens.Community.route)},
                selected = false
            )
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color(0xFF68ADD1)
                ),
                icon = { Icon(
                    painter = painterResource(id = R.drawable.profile_icon),
                    contentDescription = null,
                    tint = Color(0xFF021D3F),
                    modifier = Modifier
                        .size(40.dp)
                ) },
                onClick = {},
                selected = true
            )

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
            }

    }
}