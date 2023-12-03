package com.example.dailysync.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun Profile(navController: NavController, auth: FirebaseAuth) {

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

        Spacer(modifier = Modifier.height(20.dp))

        // body

        // TODO

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