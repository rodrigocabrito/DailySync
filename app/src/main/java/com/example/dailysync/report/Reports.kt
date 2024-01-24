package com.example.dailysync.report

import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.yml.charts.ui.barchart.BarChart
import com.example.dailysync.R
import com.example.dailysync.navigation.Screens
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(34)
@Composable
fun Reports(navController: NavController, selectedExerciseShow: Int, auth: FirebaseAuth) {

    var reportPeriodDaily by remember { mutableStateOf(true) }
    var reportPeriodWeekly by remember { mutableStateOf(false) }
    var reportPeriodMonthly by remember { mutableStateOf(false) }

    var selectedExercise by remember { mutableIntStateOf(selectedExerciseShow)}            // 1 = Run, 2 = Walk, 3 = Cycle
    var selectedPeriod by remember { mutableIntStateOf(1)}                           // 1 = Daily, 2 = Weekly, 3 = Monthly

    val titleChart = when (selectedExercise) {
        1 -> "Run"
        2 -> "Walk"
        else -> "Cycle"
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
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

        val title = if (reportPeriodDaily) "Daily" else if (reportPeriodWeekly) "Weekly" else "Monthly"
        Text(text = "Your $title Report", fontSize = 30.sp, color = Color(0xFF11435C))

        Spacer(modifier = Modifier.height(10.dp))

        // body
        // period filter
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 2.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .padding(end = 16.dp)
                    .clickable {
                        reportPeriodDaily = true
                        reportPeriodWeekly = false
                        reportPeriodMonthly = false
                        selectedPeriod = 1
                    }
                    .background(
                        if (reportPeriodDaily) Color(0xFF2C8CBC) else Color(0xFFA2D6F0),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(2.dp, Color(0xFF2C8CBC), shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Daily")
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .padding(end = 16.dp)
                    .clickable {
                        reportPeriodDaily = false
                        reportPeriodWeekly = true
                        reportPeriodMonthly = false
                        selectedPeriod = 2
                    }
                    .background(
                        if (reportPeriodWeekly) Color(0xFF2C8CBC) else Color(0xFFA2D6F0),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(2.dp, Color(0xFF2C8CBC), shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Weekly")
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .padding(end = 16.dp)
                    .clickable {
                        reportPeriodDaily = false
                        reportPeriodWeekly = false
                        reportPeriodMonthly = true
                        selectedPeriod = 3
                    }
                    .background(
                        if (reportPeriodMonthly) Color(0xFF2C8CBC) else Color(0xFFA2D6F0),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(2.dp, Color(0xFF2C8CBC), shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Monthly")
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Exercise title and arrows
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = {
                    if (selectedExercise != 1) {
                        selectedExercise--

                        navController.navigate(
                            Screens.Reports.route
                                .replace(
                                    oldValue = "{selectedExercise}",
                                    newValue = "$selectedExercise"
                                )
                        )
                    }
                },
                modifier = Modifier
                    .height(25.dp)
                    .weight(1f)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF154E1C))
            }

            Spacer(modifier = Modifier.width(40.dp))

            Text(
                text = titleChart,
                fontSize = 20.sp,
                modifier = Modifier.weight(0.5f),
                color = Color(0xFF154E1C),
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = {
                    navController.navigate(
                        Screens.ExerciseReport.route
                            .replace(
                                oldValue = "{selectedExercise}",
                                newValue = selectedExercise.toString()
                            )
                            .replace(
                                oldValue = "{selectedPeriod}",
                                newValue = selectedPeriod.toString()
                            )
                    )
                },
                modifier = Modifier
                    .height(25.dp)
                    .weight(0.2f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.view_details),
                    contentDescription = null,
                    tint = Color(0xFF154E1C),
                    modifier = Modifier
                        .size(25.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            IconButton(
                onClick = {
                    if (selectedExercise != 3) {
                        selectedExercise++

                        navController.navigate(
                            Screens.Reports.route
                                .replace(
                                    oldValue = "{selectedExercise}",
                                    newValue = "$selectedExercise"
                                )
                        )
                    }
                },
                modifier = Modifier
                    .height(25.dp)
                    .weight(1f)
            ) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Forward", tint = Color(0xFF154E1C))
            }
        }

        // graphs
        Box(
            modifier = Modifier
                .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
                .fillMaxWidth()
                .border(2.dp, Color(0xFF1A8B47), shape = RoundedCornerShape(8.dp)),
        ) {
            BarChart(
                modifier = Modifier
                    .height(150.dp)
                    .border(2.dp, Color(0xFF1A8B47), shape = RoundedCornerShape(8.dp)),
                barChartData = barChartExercise(selectedExercise, if (reportPeriodDaily) 1 else if (reportPeriodWeekly) 2 else 3, auth)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Sleep",
                fontSize = 20.sp,
                color = Color(0xFF4F1E7E),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 40.dp)
            )
            IconButton(
                onClick = {
                    navController.navigate(
                        Screens.SleepReport.route
                            .replace(
                                oldValue = "{selectedPeriod}",
                                newValue = selectedPeriod.toString()
                            )
                    )
                },
                modifier = Modifier
                    .height(25.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.view_details),
                    contentDescription = null,
                    tint = Color(0xF14B3283),
                    modifier = Modifier
                        .size(25.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(2.dp, Color(0xF14B3283), shape = RoundedCornerShape(8.dp)),
        ) {
            BarChart(
                modifier = Modifier
                    .height(150.dp),
                barChartData = barChartSleep(if (reportPeriodDaily) 1 else if (reportPeriodWeekly) 2 else 3, auth))
        }


        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Read",
                fontSize = 20.sp,
                color = Color(0xFF91641F),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 40.dp)
            )
            IconButton(
                onClick = {
                    navController.navigate(
                        Screens.ReadReport.route
                            .replace(
                                oldValue = "{selectedPeriod}",
                                newValue = selectedPeriod.toString()
                            )
                    )
                },
                modifier = Modifier
                    .height(25.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.view_details),
                    contentDescription = null,
                    tint = Color(0xFF91641F),
                    modifier = Modifier
                        .size(35.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
                .fillMaxWidth()
                .border(2.dp, Color(0xFF91641F), shape = RoundedCornerShape(8.dp)),
        ) {
            BarChart(
                modifier = Modifier
                    .height(150.dp),
                barChartData = barChartRead(if (reportPeriodDaily) 1 else if (reportPeriodWeekly) 2 else 3, auth))
        }

        //fix footer in the bottom
        Spacer(modifier = Modifier.weight(1f))

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
                onClick = {},
                selected = true
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
                onClick = {navController.navigate(Screens.Community.route)},
                selected = false
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