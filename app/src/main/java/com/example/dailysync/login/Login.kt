package com.example.dailysync.login

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
import androidx.navigation.NavHostController
import com.example.dailysync.R
import com.example.dailysync.navigation.Screens
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(navController: NavHostController, auth: FirebaseAuth) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var loginFail = false

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
            onValueChange = { email = it },
            label = { Text("Email", color = Color.DarkGray) },
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
            label = { Text("Password", color = Color.DarkGray) },
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

        Spacer(modifier = Modifier.height(10.dp))

        TextButton( modifier = Modifier
            .align(Alignment.End),
            onClick = {}
        ) {
            Text("Forgot password?", color = Color(0xFF0455BF))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(
                    Color(android.graphics.Color.parseColor("#A2D6F0")),
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable {
                    // Perform login
                    isLoading = true
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            isLoading = false
                            if (task.isSuccessful) {
                                navController.navigate(Screens.Home.route)
                            } else {
                                loginFail = true
                            }
                        }
                }
                .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
        ) {
            Text(
                "Login",
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxHeight()
                    .wrapContentSize(Alignment.Center)
            )
        }

        if (loginFail) {
           Text(
               text = "Login Failed: Your email or password is incorrect!",
               color = Color.Red
           )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.navigate(Screens.SignUp.route) }) {
            Text("Don't have an account?", color = Color.DarkGray)
                    Text("Register here.", color = Color(0xFF0455BF))
        }
    }
}