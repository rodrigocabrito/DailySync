package com.example.dailysync.home.sleep

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dailysync.R
import com.example.dailysync.navigation.Screens

@Composable
fun DefineSleepSchedule(navController: NavController, bedTimeShow: String, awakeTimeShow: String, targetShow: Int) {

    var targetValue by remember { mutableIntStateOf(targetShow) }

    val bedTime by remember { mutableStateOf(bedTimeShow) }
    val awakeTime by remember { mutableStateOf(awakeTimeShow) }

    // Function to format hours and minutes
    fun formatHoursAndMinutes(valueInHalfHours: Int): String {
        val hours = valueInHalfHours / 2
        val minutes = (valueInHalfHours % 2) * 30
        if (minutes == 0) {
            return "$hours" + "h00"
        }
        return "$hours" + "h" + "$minutes"
    }

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
                .padding(start = 10.dp, end = 16.dp, top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color(0xF1301E58)
                )
            ) { Icon(Icons.Default.ArrowBack, "Back") }

            IconButton(
                onClick = {
                    navController.navigate(Screens.Home.route)
                },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color(0xF1301E58)
                )
            ) {
                Icon(Icons.Default.Home, "Home")
            }

            Spacer(modifier = Modifier.weight(1f))
            FloatingActionButton(
                modifier = Modifier
                    .size(60.dp)
                    .border(2.dp, Color(0xF14B3283), RoundedCornerShape(15.dp)),
                onClick = {
                    navController.navigate(Screens.RegisterSleep.route)
                },
                containerColor = Color(0xFFCCBCEE),
                contentColor = Color(0xF1301E58),
            ) {
                Icon(
                    modifier = Modifier.size(35.dp),
                    painter = painterResource(id = R.drawable.register_sleep),
                    contentDescription = "Start or register reading session",
                )
            }
        }

        // body
        Spacer(modifier = Modifier.height(30.dp))

        Text(text = "Define Sleep Schedule", fontSize = 30.sp, color = Color(0xF1301E58))

        Spacer(modifier = Modifier.height(90.dp))

        // target
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(
                    Color(android.graphics.Color.parseColor("#F3F3F3")),
                    shape = RoundedCornerShape(8.dp)
                )
                .border(2.dp, Color(0xF14B3283), RoundedCornerShape(15.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Target",
                fontSize = 20.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 26.dp)
            )

            // Editable int value with buttons
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Button to increase value
                    IconButton(
                        onClick = {
                            if (targetValue <= 46.5) {
                                targetValue++
                            }
                        }
                    ) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase")
                    }

                    // Editable int value
                    Text(
                        text = formatHoursAndMinutes(targetValue),
                        fontSize = 20.sp,
                        color = Color(0xFF5931B0)
                    )

                    // Button to decrease value
                    IconButton(
                        onClick = {
                            if (targetValue >= 1) {
                                targetValue--
                            }
                        }
                    ) {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease")
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))

        // schedule
        Text(
            text = "Schedule",
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 35.dp)
        )

        Spacer(modifier = Modifier.height(5.dp))

        Box(
            modifier = Modifier
                .height(130.dp)
                .width(356.dp)
                .background(
                    Color(android.graphics.Color.parseColor("#F3F3F3")),
                    shape = RoundedCornerShape(8.dp)
                )
                .border(2.dp, Color(0xF14B3283), RoundedCornerShape(15.dp)),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 25.dp, top = 20.dp, end = 10.dp)
            ) {
                Row {
                    Text(text = "\uD83D\uDECF ️Bed Time", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(100.dp))
                    Text(text = "⏰ Awake Time", fontSize = 12.sp)
                }

                Row {
                    if (bedTime == "1") {
                        Text(text = "$bedTime:00", fontSize = 25.sp)
                    } else {
                        Text(text = bedTime, fontSize = 25.sp)
                    }
                    Spacer(modifier = Modifier.width(130.dp))

                    if (awakeTime == "1") {
                        Text(text = "$awakeTime:00", fontSize = 25.sp)
                    } else {
                        Text(text = awakeTime, fontSize = 25.sp)
                    }
                }

                Spacer(modifier = Modifier.height(5.dp))

                Divider(color = Color(0xFFDDDDDD), thickness = 2.dp, modifier = Modifier.fillMaxWidth())

                TextButton(onClick = {
                    navController.navigate(Screens.EditSleepSchedule.route
                        .replace(
                            oldValue = "{target}",
                            newValue = formatHoursAndMinutes(targetValue)
                        )
                    )
                }) {
                    Text(
                        text = "Edit",
                        fontSize = 22.sp,
                        textAlign = TextAlign.Start,
                        color = Color(0xFF5931B0)
                    )
                }
            }
        }
    }
}