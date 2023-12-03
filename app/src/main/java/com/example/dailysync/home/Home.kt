package com.example.dailysync.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dailysync.R
import com.example.dailysync.navigation.Screens
import com.example.dailysync.ui.theme.DailySyncTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun Home(navController: NavController, auth: FirebaseAuth) {

    //Text("Welcome ${auth.currentUser?.email}")

    var showExerciseOptions by remember { mutableStateOf(false) }
    var showSleepOptions by remember { mutableStateOf(false) }
    var showReadOptions by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        {
            Text(text = "HEADER")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // body

        // Exercise
        if (!showExerciseOptions) {
            Button(
                onClick = {
                    showExerciseOptions = true
                    showSleepOptions = false
                    showReadOptions = false
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                    .height(175.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(8.dp)),
            ) {
                Text("Exercise")
            }
        }

        // exercise options appear when Exercise button is clicked
        if (showExerciseOptions) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                    .background(Color.Green, shape = RoundedCornerShape(8.dp))
                    .height(175.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Green, shape = RoundedCornerShape(8.dp))
                ) {

                    Spacer(modifier = Modifier.height(4.dp))

                    // Button 1
                    Button(
                        onClick = { /* Handle button click */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Walk")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Button 2
                    Button(
                        onClick = { /* Handle button click */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Run")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Button 3
                    Button(
                        onClick = { /* Handle button click */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Cycle")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sleep
        if (!showSleepOptions) {
            Button(
                onClick = {
                    showExerciseOptions = false
                    showSleepOptions = true
                    showReadOptions = false
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                    .height(175.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(8.dp))
            ) {
                Text("Sleep")
            }
        }

        // sleep options appear when Sleep button is clicked
        if (showSleepOptions) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                    .background(Color.Green, shape = RoundedCornerShape(8.dp))
                    .height(175.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Green, shape = RoundedCornerShape(8.dp))
                ) {

                    Spacer(modifier = Modifier.height(8.dp))

                    // Button 1
                    Button(
                        onClick = { /* Handle button click */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(75.dp)
                    ) {
                        Text("Register Today's Sleep")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Button 2
                    Button(
                        onClick = { /* Handle button click */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(75.dp)
                    ) {
                        Text("Define Sleep Schedule")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Read
        if (!showReadOptions) {
            Button(
                onClick = {
                    showExerciseOptions = false
                    showSleepOptions = false
                    showReadOptions = true
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                    .height(175.dp)
                    .background(Color.Gray, shape = RoundedCornerShape(8.dp))
            ) {
                Text("Read")
            }
        }

        // read options appear when Read button is clicked
        if (showReadOptions) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                    .background(Color.Green, shape = RoundedCornerShape(8.dp))
                    .height(175.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Green, shape = RoundedCornerShape(8.dp))
                ) {

                    Spacer(modifier = Modifier.height(4.dp))

                    // Button 1
                    Button(
                        onClick = { /* Handle button click */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Find Books")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Button 2
                    Button(
                        onClick = { /* Handle button click */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Currently Reading")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Button 3
                    Button(
                        onClick = { /* Handle button click */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Wishlist")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }

            Button(
                onClick = { navController.navigate(Screens.Reports.route) },
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
                    .background(Color.Black)
            ) {
                Text("Report")
            }

            Button(
                onClick = { navController.navigate(Screens.Community.route) },
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
                    .background(Color.Gray)
            ) {
                Text("Community",
                    fontSize = 9.sp,
                    textAlign = TextAlign.Center
                )
            }

            Button(
                onClick = { navController.navigate(Screens.Profile.route) },
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
                    .background(Color.Black)
            ) {
                Text("Profile")
            }
        }
    }
}