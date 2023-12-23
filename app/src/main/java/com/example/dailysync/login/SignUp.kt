package com.example.dailysync.login

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.dailysync.R
import com.example.dailysync.User
import com.example.dailysync.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUp(navController: NavHostController, auth: FirebaseAuth) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("")}
    var isLoading by remember { mutableStateOf(false) }
    var signupFail by remember { mutableStateOf (false)}

    Image(
        painter = painterResource(id = R.drawable.fulllogo),
        contentDescription = null,
        modifier = Modifier.fillMaxWidth()
            .size(150.dp)
            .padding(top = 80.dp)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name", color = Color.Gray) },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.DarkGray,
                containerColor = Color(android.graphics.Color.parseColor("#E6F3F3")),
                cursorColor = Color(0xFF0455BF),
                focusedIndicatorColor = Color(0xFF0455BF),
                unfocusedIndicatorColor = Color(0xFF0455BF)
            ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = Color.Gray) },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.DarkGray,
                containerColor = Color(android.graphics.Color.parseColor("#E6F3F3")),
                cursorColor = Color(0xFF0455BF),
                focusedIndicatorColor = Color(0xFF0455BF),
                unfocusedIndicatorColor = Color(0xFF0455BF)
            ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = Color.Gray) },
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.DarkGray,
                containerColor = Color(android.graphics.Color.parseColor("#E6F3F3")),
                cursorColor = Color(0xFF0455BF),
                focusedIndicatorColor = Color(0xFF0455BF),
                unfocusedIndicatorColor = Color(0xFF0455BF)
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .width(280.dp)
                .height(48.dp)
                .background(
                    Color(android.graphics.Color.parseColor("#A2D6F0")),
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable {
                    // Perform registration
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // User registration successful
                                val userId = auth.currentUser?.uid
                                userId?.let { uid ->
                                    val user = User(name, email, password)
                                    val profileUpdate = UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build()
                                    auth.currentUser?.updateProfile(profileUpdate)
                                        ?.addOnCompleteListener { profileUpdateTask ->
                                            if (profileUpdateTask.isSuccessful) {
                                                writeUserToDatabase(uid, user)
                                                navController.navigate(Screens.Home.route)
                                            } else {
                                                signupFail = true
                                            }
                                        }
                                }
                            } else {
                                // Handle the case where user registration failed
                                signupFail = true
                            }
                        }
                }
                .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
        ) {
            Text(
                "Register",
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxHeight()
                    .wrapContentSize(Alignment.Center)
            )
        }

        if (signupFail) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Signup failed: Email already registered!",
                fontSize = 11.sp,
                color = Color.Red
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.navigate(Screens.Login.route) }) {
            Text("Already have an account?", color = Color.DarkGray)
            Text(" Login here", color = Color(0xFF0455BF))
        }
    }
}

private fun writeUserToDatabase(userId: String, user: User) {
    val database = Firebase.database
    val usersRef = database.getReference("users")
    usersRef.child(userId).setValue(user)
}