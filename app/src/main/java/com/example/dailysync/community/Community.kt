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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.dailysync.CommunityPost
import com.example.dailysync.Exercise
import com.example.dailysync.R
import com.example.dailysync.navigation.Screens
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import com.google.firebase.storage.storage

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

        //Show Community Posts
        CommunityPosts()

        //Button Share Your Own
        /*Box(
            modifier = Modifier
                .width(300.dp)
                .height(50.dp)
                .padding(16.dp)
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
        }*/
        /*/ExtendedFloatingActionButton(
            onClick = { navController.navigate(Screens.SelectToShare.route) },
            icon = { Icon(
                modifier = Modifier.size(35.dp),
                painterResource(id = R.drawable.share_icon
                ), "share icon") },
            text = { Text(text = "Share Your Own") },
        )*/




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

@Composable
fun CommunityPosts() {
    val database = Firebase.database
    val communityPath = database.getReference("community")
    var communityPostData: List<CommunityPost> by remember { mutableStateOf(emptyList()) }

    // Use LaunchedEffect to fetch data asynchronously
    LaunchedEffect(Unit) {
        communityPath.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val dataList = mutableListOf<CommunityPost>()
                for (i in snapshot.children) {
                    val posts = i.getValue(CommunityPost::class.java)
                    posts?.let {
                        dataList.add(it)
                    }
                }
                communityPostData += dataList
            }
        }
    }

    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        items(items = communityPostData, itemContent = { item ->
            if(item.type == "Exercise") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(8.dp)
                        .background(
                            Color(android.graphics.Color.parseColor("#A2F0C1")),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp)),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row{
                            val storage = Firebase.storage
                            val storageReference = storage.reference.child("profile_images/${item.profilePhoto}")
                            val imageUrlState = remember { mutableStateOf<String?>(null) }
                            storageReference.downloadUrl.addOnSuccessListener {
                                imageUrlState.value = it.toString()
                            }.addOnFailureListener {

                            }
                            Image(
                                painter = imageUrlState.value?.let { rememberAsyncImagePainter(model = it) }
                                    ?: rememberAsyncImagePainter(model = R.drawable.default_profile),
                                contentScale = ContentScale.Crop,
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape)
                            )
                            Column(modifier = Modifier.padding(start = 8.dp)) {
                                Text(
                                    text = item.userName,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                                Text(
                                    text = "Exercise: ${item.exerciseType} - ${item.date}",
                                    fontSize = 15.sp
                                )
                            }
                        }
                        Row(modifier = Modifier.padding(top = 8.dp)) {
                            Column(modifier = Modifier.padding(end = 10.dp)) {
                                Text(
                                    text = "Distance",
                                    fontSize = 15.sp,
                                )
                                Text(
                                    text = "${item.distance}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column(modifier = Modifier.padding(end = 10.dp)) {
                                Text(
                                    text = "Time",
                                    fontSize = 15.sp,
                                )
                                Text(
                                    text = "${item.time}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column {
                                Text(
                                    text = "Rhythm",
                                    fontSize = 15.sp,
                                )
                                Text(
                                    text = "${item.rhythm}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }else if(item.type == "Sleep") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(8.dp)
                        .background(
                            Color(android.graphics.Color.parseColor("#CCBCEE")),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp)),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row{
                            val storage = Firebase.storage
                            val storageReference = storage.reference.child("profile_images/${item.profilePhoto}")
                            val imageUrlState = remember { mutableStateOf<String?>(null) }
                            storageReference.downloadUrl.addOnSuccessListener {
                                imageUrlState.value = it.toString()
                            }.addOnFailureListener {

                            }
                            Image(
                                painter = imageUrlState.value?.let { rememberAsyncImagePainter(model = it) }
                                    ?: rememberAsyncImagePainter(model = R.drawable.default_profile),
                                contentScale = ContentScale.Crop,
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape)
                            )
                            Column(modifier = Modifier.padding(start = 8.dp)) {
                                Text(
                                    text = item.userName,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                                Text(
                                    text = "${item.type} - ${item.date}",
                                    fontSize = 15.sp
                                )
                            }
                        }
                        Row(modifier = Modifier.padding(top = 8.dp)) {
                            Column(modifier = Modifier.padding(end = 10.dp)) {
                                Text(
                                    text = "Bed Time",
                                    fontSize = 15.sp,
                                )
                                Text(
                                    text = "${item.bedTime}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column(modifier = Modifier.padding(end = 10.dp)) {
                                Text(
                                    text = "Wake Time",
                                    fontSize = 15.sp,
                                )
                                Text(
                                    text = "${item.wakeTime}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column {
                                Text(
                                    text = "Sleep Time",
                                    fontSize = 15.sp,
                                )
                                Text(
                                    text = "${item.sleepTime}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            } else if(item.type == "Read") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(8.dp)
                        .background(
                            Color(android.graphics.Color.parseColor("#F5D4A2")),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp)),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row{
                            val storage = Firebase.storage
                            val storageReference = storage.reference.child("profile_images/${item.profilePhoto}")
                            val imageUrlState = remember { mutableStateOf<String?>(null) }
                            storageReference.downloadUrl.addOnSuccessListener {
                                imageUrlState.value = it.toString()
                            }.addOnFailureListener {

                            }
                            Image(
                                painter = imageUrlState.value?.let { rememberAsyncImagePainter(model = it) }
                                    ?: rememberAsyncImagePainter(model = R.drawable.default_profile),
                                contentScale = ContentScale.Crop,
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape)
                            )
                            Column(modifier = Modifier.padding(start = 8.dp)) {
                                Text(
                                    text = item.userName,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                                Text(
                                    text = "${item.type} - ${item.date}",
                                    fontSize = 15.sp
                                )
                            }
                        }
                        Row(modifier = Modifier.padding(top = 8.dp)) {
                            Column(modifier = Modifier.padding(end = 10.dp)) {
                                Text(
                                    text = "Book Name",
                                    fontSize = 15.sp,
                                )
                                Text(
                                    text = "${item.bookName}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column(modifier = Modifier.padding(end = 10.dp)) {
                                Text(
                                    text = "Pages Read",
                                    fontSize = 15.sp,
                                )
                                Text(
                                    text = "${item.pagesRead}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column {
                                Text(
                                    text = "Time Spent",
                                    fontSize = 15.sp,
                                )
                                Text(
                                    text = "${item.timeSpent}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        })
    }
}