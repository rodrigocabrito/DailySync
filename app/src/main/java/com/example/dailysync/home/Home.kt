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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dailysync.R
import com.example.dailysync.navigation.Screens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Home(navController: NavController, auth: FirebaseAuth) {

    //Text("Welcome ${auth.currentUser?.email}")

    var showExerciseOptions by remember { mutableStateOf(false) }
    var showSleepOptions by remember { mutableStateOf(false) }
    var showReadOptions by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
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
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center, // Center vertically
                    horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
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
                            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
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
                            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
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
                            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
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
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
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
                                    Screens.RegisterSleep.route // TODO CHANGE ARGS
                                )
                            }
                            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
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
                                            newValue = "16"                     // TODO GET FROM DATABASE?
                                        )
                                )
                            }
                            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
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
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
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
                            .height(50.dp)
                            .background(
                                Color(android.graphics.Color.parseColor("#E5AE5A")),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                // TODO ADD NAVIGATION & ARGS
                            }
                            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
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
                            Spacer(modifier = Modifier.width(170.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.search_icon),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Currently Reading
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(
                                Color(android.graphics.Color.parseColor("#E5AE5A")),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                // TODO ADD NAVIGATION & ARGS
                            }
                            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Currently Reading",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                            Spacer(modifier = Modifier.width(100.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.reading_icon),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(60.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Wishlist
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(
                                Color(android.graphics.Color.parseColor("#E5AE5A")),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                // TODO ADD NAVIGATION & ARGS
                            }
                            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Wishlist",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                            Spacer(modifier = Modifier.width(190.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.wishlist_icon),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(40.dp)
                            )
                        }
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
                    .background(Color(android.graphics.Color.parseColor("#2C8CBC")))
                    .clickable {
                        navController.navigate(Screens.Home.route)
                    }
                    .border(1.dp, Color.Black)
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
                        modifier = Modifier
                            .size(35.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text( "Home")

                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
                    .background(Color(android.graphics.Color.parseColor("#A2D6F0")))
                    .clickable {
                        navController.navigate(Screens.Reports.route)
                    }
                    .border(1.dp, Color.Black)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center, // Center vertically
                    horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.report_icon),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier
                            .size(35.dp)
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 10.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text( text ="Report")
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
                    .background(Color(android.graphics.Color.parseColor("#A2D6F0")))
                    .clickable {
                        navController.navigate(Screens.Community.route)
                    }
                    .border(1.dp, Color.Black)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center, // Center vertically
                    horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.community_icon),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier
                            .size(45.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Text( text = "Community",
                        fontSize = 14.sp)
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp)
                    .background(Color(android.graphics.Color.parseColor("#A2D6F0")))
                    .clickable {
                        navController.navigate(Screens.Profile.route)
                    }
                    .border(1.dp, Color.Black)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top=10.dp),
                    verticalArrangement = Arrangement.Center, // Center vertically
                    horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.profile_icon),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text( "Profile")
                }
            }
        }
    }
}