package com.example.dailysync.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dailysync.R
import com.example.dailysync.SleepTarget
import com.example.dailysync.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Composable
fun Home(navController: NavController, auth: FirebaseAuth) {

    var showExerciseOptions by remember { mutableStateOf(false) }
    var showSleepOptions by remember { mutableStateOf(false) }
    var showReadOptions by remember { mutableStateOf(false) }
    val userId = auth.currentUser?.uid

    Column(
        modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // name
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 30.dp)
        )
        {
            auth.currentUser?.displayName?.let { Text(text = "Hey $it,", fontSize = 25.sp, fontWeight = FontWeight.SemiBold) }
        }

        Spacer(modifier = Modifier.height(5.dp))

        // body

        // Exercise
        if (!showExerciseOptions) {
            Box(
                modifier = Modifier
                    .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                    .weight(1f)
                    .height(175.dp)
                    .background(
                        Color(android.graphics.Color.parseColor("#47E285")),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        showExerciseOptions = true
                        showSleepOptions = false
                        showReadOptions = false
                    }
                    .border(2.dp, Color(0xFF1A8B47), shape = RoundedCornerShape(8.dp))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.exercise_icon),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Text(
                    text = "Start Exercise",
                    modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        // exercise options appear when Exercise button is clicked
        if (showExerciseOptions) {
            Box(
                modifier = Modifier
                    .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                    .weight(1f)
                    .height(175.dp)
                    .background(Color.Transparent)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Spacer(modifier = Modifier.height(4.dp))

                    // Walk
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(
                                Color(android.graphics.Color.parseColor("#47E285")),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                navController.navigate(
                                    Screens.StartExercise.route
                                        .replace(
                                            oldValue = "{category}",
                                            newValue = "1"
                                        )
                                )
                            }
                            .border(2.dp, Color(0xFF1A8B47), shape = RoundedCornerShape(8.dp))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Walk",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                            Spacer(modifier = Modifier.width(180.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.walk_icon),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Run
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(
                                Color(android.graphics.Color.parseColor("#47E285")),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                navController.navigate(
                                    Screens.StartExercise.route
                                        .replace(
                                            oldValue = "{category}",
                                            newValue = "2"
                                        )
                                )
                            }
                            .border(2.dp, Color(0xFF1A8B47), shape = RoundedCornerShape(8.dp))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Run",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                            Spacer(modifier = Modifier.width(180.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.run_icon),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Cycle
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(
                                Color(android.graphics.Color.parseColor("#47E285")),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                navController.navigate(
                                    Screens.StartExercise.route
                                        .replace(
                                            oldValue = "{category}",
                                            newValue = "3"
                                        )
                                )
                            }
                            .border(2.dp, Color(0xFF1A8B47), shape = RoundedCornerShape(8.dp))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Cycle",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                            Spacer(modifier = Modifier.width(180.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.cycle_icon),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sleep
        if (!showSleepOptions) {
            Box(
                modifier = Modifier
                    .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                    .weight(1f)
                    .height(175.dp)
                    .background(
                        Color(android.graphics.Color.parseColor("#A17FEB")),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        showExerciseOptions = false
                        showSleepOptions = true
                        showReadOptions = false
                    }
                    .border(2.dp, Color(0xF14B3283), shape = RoundedCornerShape(8.dp))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center, // Center vertically
                    horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.sleep_icon),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = "Sleep",
                        modifier = Modifier
                            .padding(start = 8.dp)
                    )
                }
            }
        }

        // sleep options appear when Sleep button is clicked
        if (showSleepOptions) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                    .background(Color.Transparent)
                    .height(175.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Spacer(modifier = Modifier.height(8.dp))

                    // Register Sleep Schedule
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(75.dp)
                            .background(
                                Color(android.graphics.Color.parseColor("#A17FEB")),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                navController.navigate(
                                    Screens.RegisterSleep.route
                                )
                            }
                            .border(2.dp, Color(0xF14B3283), shape = RoundedCornerShape(8.dp))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Register Today's Sleep",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                            Spacer(modifier = Modifier.width(100.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.register_sleep_icon),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Define Sleep Schedule
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(75.dp)
                            .background(
                                Color(android.graphics.Color.parseColor("#A17FEB")),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {

                                val database = Firebase.database

                                userId
                                    ?.let {
                                        database
                                            .getReference("users")
                                            .child(it)
                                            .child("SleepTarget")
                                    }
                                    ?.addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                val sleepTarget =
                                                    dataSnapshot.getValue(SleepTarget::class.java)

                                                val timeTarget = sleepTarget?.timeTarget

                                                val hourBedTime = sleepTarget?.bedTimeHour
                                                val minBedTime = sleepTarget?.bedTimeMin

                                                val hourAwakeTime = sleepTarget?.awakeTimeHour
                                                val minAwakeTime = sleepTarget?.awakeTimeMin

                                                if (timeTarget != null) {
                                                    navController.navigate(
                                                        Screens.DefineSleepSchedule.route
                                                            .replace(
                                                                oldValue = "{bedTime}",
                                                                newValue = "$hourBedTime:$minBedTime"
                                                            )
                                                            .replace(
                                                                oldValue = "{awakeTime}",
                                                                newValue = "$hourAwakeTime:$minAwakeTime"
                                                            )
                                                            .replace(
                                                                oldValue = "{target}",
                                                                newValue = "$timeTarget"
                                                            )
                                                    )
                                                }
                                            } else {
                                                navController.navigate(
                                                    Screens.DefineSleepSchedule.route
                                                        .replace(
                                                            oldValue = "{bedTime}",
                                                            newValue = "1"
                                                        )
                                                        .replace(
                                                            oldValue = "{awakeTime}",
                                                            newValue = "1"
                                                        )
                                                        .replace(
                                                            oldValue = "{target}",
                                                            newValue = "16"
                                                        )
                                                )
                                            }
                                        }

                                        override fun onCancelled(databaseError: DatabaseError) {
                                            // Handle errors
                                            println("Error: ${databaseError.message}")
                                        }
                                    })
                            }
                            .border(2.dp, Color(0xF14B3283), shape = RoundedCornerShape(8.dp))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Define Sleep Schedule",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                            Spacer(modifier = Modifier.width(100.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.schedule_icon),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Read
        if (!showReadOptions) {
            Box(
                modifier = Modifier
                    .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                    .weight(1f)
                    .height(175.dp)
                    .background(
                        Color(android.graphics.Color.parseColor("#E5AE5A")),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        showExerciseOptions = false
                        showSleepOptions = false
                        showReadOptions = true
                    }
                    .border(2.dp, Color(0xFF91641F), shape = RoundedCornerShape(8.dp))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center, // Center vertically
                    horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.read_icon),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = "Read",
                        modifier = Modifier
                            .padding(start = 8.dp)
                    )
                }
            }
        }

        // read options appear when Read button is clicked
        if (showReadOptions) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 10.dp, end = 10.dp)
                    .background(Color.Transparent)
                    .height(175.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Spacer(modifier = Modifier.height(4.dp))

                    // Find Books
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(75.dp)
                            .background(
                                Color(android.graphics.Color.parseColor("#E5AE5A")),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                navController.navigate(Screens.Search.route)
                            }
                            .border(2.dp, Color(0xFF91641F), shape = RoundedCornerShape(8.dp))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Find Books",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                            Spacer(modifier = Modifier.width(165.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.search_icon),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(60.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // My library
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(75.dp)
                            .background(
                                Color(android.graphics.Color.parseColor("#E5AE5A")),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                navController.navigate(Screens.MyLibrary.route)
                            }
                            .border(2.dp, Color(0xFF91641F), shape = RoundedCornerShape(8.dp))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "My library",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                            Spacer(modifier = Modifier.width(170.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.reading_icon),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(60.dp)
                            )
                        }
                    }

                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


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
                onClick = {},
                selected = true
            )
            NavigationBarItem(
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
                icon = { Icon(
                    painter = painterResource(id = R.drawable.community_icon),
                    contentDescription = null,
                    tint = Color(0xFF021D3F),
                    modifier = Modifier
                        .size(55.dp)
                ) },
                onClick = {navController.navigate(Screens.Community.route)},
                selected = false
            )
            NavigationBarItem(
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