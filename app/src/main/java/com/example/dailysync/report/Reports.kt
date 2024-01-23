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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import co.yml.charts.axis.AxisData
import co.yml.charts.axis.DataCategoryOptions
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
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

    var selectedExercise by remember { mutableIntStateOf(1)}            // 1 = Run, 2 = Walk, 3 = Cycle

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

        Text(text = "Your Weekly Report", fontSize = 20.sp)

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
                    }
                },
                modifier = Modifier
                    .height(25.dp)
                    .weight(1f)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF154E1C))
            }

            Spacer(modifier = Modifier.width(70.dp))

            Text(
                text = titleChart,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f),
                color = Color(0xFF154E1C),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(10.dp))

            IconButton(
                onClick = {
                    if (selectedExercise != 3) {
                        selectedExercise++
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
                .clickable {
                    navController.navigate(Screens.ExerciseReport.route)            // TODO get args?
                }
                .border(2.dp, Color(0xFF1A8B47), shape = RoundedCornerShape(8.dp)),
        ) {
            val maxRange = 50                       // TODO get from database (Ex.: user max distance done + 10%)
            val barChartListSize = 7                // TODO change based of daily/weekly/monthly?
            val yStepSize = 5

            BarChart(
                modifier = Modifier
                    .height(150.dp)
                    .border(2.dp, Color(0xFF1A8B47), shape = RoundedCornerShape(8.dp)),
                barChartData = barChart(maxRange, barChartListSize, yStepSize, 1))
        }

        Text(
            text = "Sleep",
            fontSize = 20.sp,
            color = Color(0xFF4F1E7E),
            fontWeight = FontWeight.Bold
        )

        Box(
            modifier = Modifier
                .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
                .fillMaxWidth()
                .clickable {
                    navController.navigate(Screens.SleepReport.route)           // TODO get args?
                }
                .border(2.dp, Color(0xF14B3283), shape = RoundedCornerShape(8.dp)),
        ) {
            val maxRange = 50                       // TODO get from database (Ex.: user max distance done + 10%)
            val barChartListSize = 7                // TODO change based of daily/weekly/monthly?
            val yStepSize = 5

            BarChart(
                modifier = Modifier
                    .height(150.dp),
                barChartData = barChart(maxRange, barChartListSize, yStepSize, 2))
        }

        Text(
            text = "Read",
            fontSize = 20.sp,
            color = Color(0xFF64610F),
            fontWeight = FontWeight.Bold
        )

        Box(
            modifier = Modifier
                .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
                .fillMaxWidth()
                .clickable {
                    navController.navigate(Screens.ReadReport.route)            // TODO get args?
                }
                .border(2.dp, Color(0xFF91641F), shape = RoundedCornerShape(8.dp)),
        ) {
            val maxRange = 50                       // TODO get from database (Ex.: user max distance done + 10%)
            val barChartListSize = 7                // TODO change based of daily/weekly/monthly?
            val yStepSize = 5

            BarChart(
                modifier = Modifier
                    .height(150.dp),
                barChartData = barChart(maxRange, barChartListSize, yStepSize, 3))
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

// TODO get an ordered list with all the exercises/reads/sleeps -> use it in the creation of the points (x<Float>(day/weekDay/month), y<Float>(value))
fun getBarChartDataUpdated(
    listSize: Int,                          // TODO change for predefined values: Ex.: Daily(7), Weekly(10), Monthly(12) or % of target
    maxRange: Int,                          // TODO change for predefined values: Ex.: Daily(40), Weekly(300), Monthly(1500) or % of target
    dataCategoryOptions: DataCategoryOptions,
    activity: Int                           // TODO update config based of activity
): List<BarData> {
    val list = arrayListOf<BarData>()
    for (index in 0 until listSize) {           // TODO index -> Days or Monday/Tuesday/... or Jan/Feb/...
        val point = Point(index.toFloat(), "%.2f".format(Random.nextDouble(1.0, maxRange.toDouble())).toFloat())        // TODO get value from db

        list.add(
            BarData(
                point = point,
                color = if (activity == 1) Color(0xFF1A8B47) else if (activity == 2) Color(0xF14B3283) else Color(0xFF91641F),
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
    yStepSize: Int,
    activity: Int
): BarChartData {

    val barData = getBarChartDataUpdated(
        barChartListSize,
        maxRange,
        DataCategoryOptions(),
        activity
    )

    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .steps(barData.size - 1)
        .backgroundColor(if (activity == 1) Color(0xFF47E285) else if (activity == 2) Color(0xFFA17FEB) else Color(0xFFE5AE5A))
        .bottomPadding(20.dp)
        .labelAndAxisLinePadding(8.dp)
        .axisLabelAngle(20f)
        .startDrawPadding(20.dp)
        .labelData { index -> barData[index].label }
        .build()

    val yAxisData = AxisData.Builder()
        .steps(yStepSize)
        .backgroundColor(if (activity == 1) Color(0xFF47E285) else if (activity == 2) Color(0xFFA17FEB) else Color(0xFFE5AE5A))
        .labelAndAxisLinePadding(10.dp)
        .axisOffset(20.dp)
        .labelData { index -> (index * (maxRange / yStepSize)).toString() }
        .build()

    return BarChartData(
        chartData = barData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        backgroundColor = if (activity == 1) Color(0xFF47E285) else if (activity == 2) Color(0xFFA17FEB) else Color(0xFFE5AE5A),
        barStyle = BarStyle(
            paddingBetweenBars = 24.dp,
            barWidth = 24.dp
        ),
        showYAxis = true,
        showXAxis = true,
        paddingEnd = 0.dp
    )
}