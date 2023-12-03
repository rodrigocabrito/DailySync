package com.example.dailysync.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dailysync.navigation.Screens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Reports(navController: NavController, auth: FirebaseAuth) {

    var reportPeriodDaily by remember { mutableStateOf(false) }
    var reportPeriodWeekly by remember { mutableStateOf(true) }
    var reportPeriodMonthly by remember { mutableStateOf(false) }

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
            // Align text to the left
            Text(
                text = "IDK",
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1f)
            )

            // Align text to the right
            Text(
                text = "Notification Icon",
                textAlign = TextAlign.End,
                modifier = Modifier
                    .weight(1f)
            )
        }

        Text(text = "Your Weekly Report")

        Spacer(modifier = Modifier.height(20.dp))

        // body

        // period filter
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
                .padding(end = 16.dp)
        ) {
            Button(
                onClick = {
                    reportPeriodDaily = true
                    reportPeriodWeekly = false
                    reportPeriodMonthly = false
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .padding(end = 16.dp)
                    .background(Color.Black, shape = RoundedCornerShape(8.dp))
            ) {
                Text(text = "Daily")
            }

            Button(
                onClick = {
                    reportPeriodDaily = false
                    reportPeriodWeekly = true
                    reportPeriodMonthly = false
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .padding(end = 16.dp)
                    .background(Color.Black, shape = RoundedCornerShape(8.dp))
            ) {
                Text(text = "Weekly")
            }

            Button(
                onClick = {
                    reportPeriodDaily = false
                    reportPeriodWeekly = false
                    reportPeriodMonthly = true
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .background(Color.Black, shape = RoundedCornerShape(8.dp))
            ) {
                Text(text = "Monthly")
            }
        }

        // graphs

        Spacer(modifier = Modifier.height(16.dp))

        // footer
        Row(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
        ) {
            Button(
                onClick = { navController.navigate(Screens.Home.route)},
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
                    .background(Color.Gray)
            ) {
                Text("Home")
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