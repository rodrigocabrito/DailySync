package com.example.dailysync.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dailysync.ui.theme.DailySyncTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun Home(navController: NavController, auth: FirebaseAuth) {

    //Text("Welcome ${auth.currentUser?.email}")

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
        Button(
            onClick = { /* Handle button click */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                .height(175.dp)
                .background(Color.Gray, shape = RoundedCornerShape(8.dp)),

        ) {
            Text("Exercise")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* Handle button click */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                .height(175.dp)
                .background(Color.Gray, shape = RoundedCornerShape(8.dp))
        ) {
            Text("Sleep")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* Handle button click */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                .height(175.dp)
                .background(Color.Gray, shape = RoundedCornerShape(8.dp))
        ) {
            Text("Read")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // footer
        Row(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
        ) {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
                    .background(Color.Gray)
            ) {
                Text("Home")
            }

            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
                    .background(Color.Black)
            ) {
                Text("Report")
            }

            Button(
                onClick = { /*TODO*/ },
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
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
                    .background(Color.Black)
            ) {
                Text("Profile")
            }
        }
    }

    /*
    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Text("Sleep")
    }

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Text("Read")
    }
    */
}