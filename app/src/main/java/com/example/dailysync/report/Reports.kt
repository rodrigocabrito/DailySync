package com.example.dailysync.report

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.yml.charts.axis.AxisData
import co.yml.charts.axis.DataCategoryOptions
import co.yml.charts.common.model.Point
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarChartType
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import com.example.dailysync.R
import com.example.dailysync.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import kotlin.random.Random

@Composable
fun Reports(navController: NavController, auth: FirebaseAuth) {

    var reportPeriodDaily by remember { mutableStateOf(true) }
    var reportPeriodWeekly by remember { mutableStateOf(false) }
    var reportPeriodMonthly by remember { mutableStateOf(false) }

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

        Text(text = "Your Weekly Report", fontSize = 20.sp)

        Spacer(modifier = Modifier.height(10.dp))

        // body
        // period filter
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .padding(end = 16.dp)
                    .background(
                        Color(android.graphics.Color.parseColor("#47E285")),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        reportPeriodDaily = true
                        reportPeriodWeekly = false
                        reportPeriodMonthly = false
                    }
                    .border(2.dp, Color(0xFF1A8B47), shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Daily")
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .padding(end = 16.dp)
                    .background(
                        Color(android.graphics.Color.parseColor("#A17FEB")),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        reportPeriodDaily = false
                        reportPeriodWeekly = true
                        reportPeriodMonthly = false
                    }
                    .border(2.dp, Color(0xF14B3283), shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Weekly")
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .padding(end = 16.dp)
                    .background(
                        Color(android.graphics.Color.parseColor("#E5AE5A")),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        reportPeriodDaily = false
                        reportPeriodWeekly = false
                        reportPeriodMonthly = true
                    }
                    .border(2.dp, Color(0xFF91641F), shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Monthly")
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(text = "Run", fontSize = 20.sp)        // TODO change based of the activity

        // graphs
        Box(                                        // TODO change background and border
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .clickable {
                    navController.navigate(Screens.ExerciseReport.route)            // TODO get args?
                }
        ) {
            val maxRange = 50                       // TODO get from database (Ex.: user max distance done + 10%)
            val barChartListSize = 7                // TODO change based of daily/weekly/monthly?
            val yStepSize = 5

            BarChart(modifier = Modifier.height(150.dp), barChartData = barChart(maxRange,barChartListSize,yStepSize))
        }

        Text(text = "Sleep", fontSize = 20.sp)

        Box(                                        // TODO change background and border
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .clickable {
                    navController.navigate(Screens.SleepReport.route)           // TODO get args?
                }
        ) {
            val maxRange = 50                       // TODO get from database (Ex.: user max distance done + 10%)
            val barChartListSize = 7                // TODO change based of daily/weekly/monthly?
            val yStepSize = 5

            BarChart(modifier = Modifier.height(150.dp), barChartData = barChart(maxRange,barChartListSize,yStepSize))
        }

        Text(text = "Read", fontSize = 20.sp)

        Box(                                        // TODO change background and border
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .clickable {
                    navController.navigate(Screens.ReadReport.route)            // TODO get args?
                }
        ) {
            val maxRange = 50                       // TODO get from database (Ex.: user max distance done + 10%)
            val barChartListSize = 7                // TODO change based of daily/weekly/monthly?
            val yStepSize = 5

            BarChart(modifier = Modifier.height(150.dp), barChartData = barChart(maxRange,barChartListSize,yStepSize))
        }



        //fix footer in the bottom
        Spacer(modifier = Modifier.weight(1f))

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
                    .background(Color(android.graphics.Color.parseColor("#A2D6F0")))
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
                    .background(Color(android.graphics.Color.parseColor("#2C8CBC")))
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
                        .padding(top = 10.dp),
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

// TODO get an ordered list with all the exercises/reads/sleeps -> use it in the creation of the points (x<Float>(day/weekDay/month), y<Float>(value))
fun getBarChartDataUpdated(
    listSize: Int,                          // TODO change for predefined values: Ex.: Daily(7), Weekly(10), Monthly(12) or % of target
    maxRange: Int,                          // TODO change for predefined values: Ex.: Daily(40), Weekly(300), Monthly(1500) or % of target
    dataCategoryOptions: DataCategoryOptions,
    activity: Int
): List<BarData> {
    val list = arrayListOf<BarData>()
    for (index in 0 until listSize) {           // TODO index -> Days or Monday/Tuesday/... or Jan/Feb/...
        val point = Point(index.toFloat(), "%.2f".format(Random.nextDouble(1.0, maxRange.toDouble())).toFloat())        // TODO get value from db

        list.add(
            BarData(
                point = point,
                color = Color(
                    Random.nextInt(256), Random.nextInt(256), Random.nextInt(256)           // TODO all the same color depending on the activity
                ),
                dataCategoryOptions = dataCategoryOptions,
                label = "Bar$index",                            // TODO update label (Day/WeekDay/Month)
            )
        )
    }

    return list
}

fun barChart(
    maxRange: Int,
    barChartListSize: Int,
    yStepSize: Int
): BarChartData {
    val barData = getBarChartDataUpdated(
        barChartListSize,
        maxRange,
        DataCategoryOptions(),
        1                                   // TODO change (1-Exercise, 2-Read, 3-Sleep)
    )

    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .steps(barData.size - 1)
        .bottomPadding(20.dp)
        .labelAndAxisLinePadding(8.dp)
        .axisLabelAngle(20f)
        .startDrawPadding(20.dp)
        .labelData { index -> barData[index].label }
        .build()

    val yAxisData = AxisData.Builder()
        .steps(yStepSize)
        .labelAndAxisLinePadding(10.dp)
        .axisOffset(20.dp)
        .labelData { index -> (index * (maxRange / yStepSize)).toString() }
        .build()

    return BarChartData(
        chartData = barData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        backgroundColor = Color.Gray,
        barStyle = BarStyle(
            paddingBetweenBars = 24.dp,
            barWidth = 24.dp
        ),
        showYAxis = true,
        showXAxis = true
    )
}