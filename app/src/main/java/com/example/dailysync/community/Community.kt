package com.example.dailysync.community

import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dailysync.R
import com.example.dailysync.navigation.Screens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Community(navController: NavController, auth: FirebaseAuth) {

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

        Text(text = "Community")

        Spacer(modifier = Modifier.height(20.dp))

        val list = listOf("A", "B", "C", "D") + ((0..25).map { it.toString() })
        LazyColumn(modifier = Modifier.weight(200f).fillMaxHeight()) {
            items(items = list, itemContent = { item ->
                when (item) {
                    "A" -> {
                        Text(text = item, style = TextStyle(fontSize = 20.sp))
                    }
                    "B" -> {
                        Button(onClick = {}) {
                            Text(text = item, style = TextStyle(fontSize = 20.sp))
                        }
                    }
                    "C" -> {
                        //Do Nothing
                    }
                    "D" -> {
                        Text(text = item)
                    }
                    else -> {
                        Text(text = item, style = TextStyle(fontSize = 20.sp))
                    }
                }
            })
        }

        //Button Share Your Own
        Box(
            modifier = Modifier
                .width(300.dp)
                .height(50.dp)
                .background(
                    Color(android.graphics.Color.parseColor("#A2D6F0")),
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable {
                    navController.navigate(Screens.SelectToShare.route)
                }
                .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
        ) {
            Text(
                "Share Your Own",
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxHeight()
                    .wrapContentSize(Alignment.Center)
            )
        }




        // footer
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
                onClick = {},
                selected = true
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
                onClick = {navController.navigate(Screens.Profile.route)},
                selected = false
            )

        }
    }
}