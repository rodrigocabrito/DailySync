package com.example.dailysync.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.dailysync.navigation.Screens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Login(navController: NavHostController, auth: FirebaseAuth) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var loginFail by remember {mutableStateOf(false)}

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
            value = email,
            onValueChange = {
                email = it
                emailError = false},
            label = { Text("Email", color = Color.Gray) },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.DarkGray,
                unfocusedTextColor = Color.DarkGray,
                focusedContainerColor = Color(android.graphics.Color.parseColor("#E6F3F3")),
                unfocusedContainerColor = Color(android.graphics.Color.parseColor("#E6F3F3")),
                cursorColor = Color(0xFF0455BF),
                focusedIndicatorColor = Color(0xFF0455BF),
                unfocusedIndicatorColor = Color(0xFF0455BF)
            ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )
        if (emailError) {
            Text(
                text = "Email cannot be empty",
                fontSize = 11.sp,
                color = Color.Red
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = false},
            label = { Text("Password", color = Color.Gray) },
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.DarkGray,
                unfocusedTextColor = Color.DarkGray,
                focusedContainerColor = Color(android.graphics.Color.parseColor("#E6F3F3")),
                unfocusedContainerColor = Color(android.graphics.Color.parseColor("#E6F3F3")),
                cursorColor = Color(0xFF0455BF),
                focusedIndicatorColor = Color(0xFF0455BF),
                unfocusedIndicatorColor = Color(0xFF0455BF)
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
            )
        )
        if (passwordError) {
            Text(
                text = "Password cannot be empty",
                fontSize = 11.sp,
                color = Color.Red
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        TextButton( modifier = Modifier
            .align(Alignment.End)
            .padding(end = 30.dp),
            onClick = {}
        ) {
            Text("Forgot password?", color = Color(0xFF0455BF))
        }

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .width(280.dp)
                .height(48.dp)
                .background(
                    Color(android.graphics.Color.parseColor("#A2D6F0")),
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable {
                    // Perform login
                    if (email.isNotBlank() && password.isNotBlank()) {
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    navController.navigate(Screens.Home.route)
                                } else {
                                    loginFail = true
                                }
                            }
                    }else{
                        emailError = email.isBlank()
                        passwordError = password.isBlank()
                        loginFail = false // Reset login fail flag
                    }
                }
                .border(2.dp, Color(0xFF033575), shape = RoundedCornerShape(8.dp))
        ) {
            Text(
                "Login",
                color = Color(0xFF033575),
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxHeight()
                    .wrapContentSize(Alignment.Center)
            )
        }

        if (loginFail) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Login Failed: Your email or password is incorrect!",
                fontSize = 11.sp,
                color = Color.Red
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.navigate(Screens.SignUp.route) }) {
            Text("Don't have an account?", color = Color.DarkGray)
                    Text(" Register here", color = Color(0xFF0455BF))
        }
    }
}