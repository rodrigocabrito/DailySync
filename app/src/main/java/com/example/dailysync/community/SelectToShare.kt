package com.example.dailysync.community

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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dailysync.R
import com.example.dailysync.navigation.Screens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SelectToShare(navController: NavController, auth: FirebaseAuth) {
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
                    navController.navigate(Screens.Home.route)
                },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.Black
                )
            ) {
                Icon(Icons.Default.Home, "Home")
            }

            Spacer(modifier = Modifier.weight(1f))

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

        Text(text = "Select What to Share", fontSize = 30.sp, color = Color(0xFF11435C))

        Spacer(modifier = Modifier.height(20.dp))

        Column {
            Row {
                Box(
                    modifier = Modifier
                        .width(300.dp)
                        .height(120.dp)
                        .background(
                            Color(android.graphics.Color.parseColor("#47E285")),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            navController.navigate(Screens.SelectFromList.route
                                .replace(oldValue = "{type}", newValue = "Exercise")
                            )
                        }
                        .border(2.dp, Color(0xFF1A8B47), shape = RoundedCornerShape(8.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxHeight()
                            .wrapContentSize(Alignment.Center)
                    ){
                        Column {
                            Image(
                                painter = painterResource(id = R.drawable.exercise_icon),
                                contentDescription = "Exercise",
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Exercise",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                ),
                                modifier = Modifier
                                    .padding(start = 10.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Box(
                    modifier = Modifier
                        .width(300.dp)
                        .height(120.dp)
                        .background(
                            Color(android.graphics.Color.parseColor("#A17FEB")),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            navController.navigate(Screens.SelectFromList.route
                                .replace(oldValue = "{type}", newValue = "Sleep")
                            )
                        }
                        .border(2.dp, Color(0xF14B3283), shape = RoundedCornerShape(8.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxHeight()
                            .wrapContentSize(Alignment.Center)
                    ){
                        Column {
                            Image(
                                painter = painterResource(id = R.drawable.sleep_icon),
                                contentDescription = "Sleep",
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Sleep",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                ),
                                modifier = Modifier
                                    .padding(start = 10.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Box(
                    modifier = Modifier
                        .width(300.dp)
                        .height(120.dp)
                        .background(
                            Color(android.graphics.Color.parseColor("#E5AE5A")),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            navController.navigate(Screens.SelectFromList.route
                                .replace(oldValue = "{type}", newValue = "Read")
                            )
                        }
                        .border(2.dp, Color(0xFF91641F), shape = RoundedCornerShape(8.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxHeight()
                            .wrapContentSize(Alignment.Center)
                    ){
                        Column {
                            Image(
                                painter = painterResource(id = R.drawable.read_icon),
                                contentDescription = "Read",
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Read",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                ),
                                modifier = Modifier
                                    .padding(start = 10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}