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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.example.dailysync.Sleep
import com.example.dailysync.SleepTarget
import com.example.dailysync.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


@RequiresApi(34)
@Composable
fun SleepReport(navController: NavController, selectedPeriodShow: Int, auth: FirebaseAuth) {

    var reportPeriodDaily by remember { mutableStateOf(false) }
    var reportPeriodWeekly by remember { mutableStateOf(false) }
    var reportPeriodMonthly by remember { mutableStateOf(false) }

    var selectedPeriod by remember { mutableIntStateOf(selectedPeriodShow) }                // 1 = Daily, 2 = Weekly, 3 = Monthly


    if (selectedPeriod == 1) {
        reportPeriodDaily = true
        reportPeriodWeekly = false
        reportPeriodMonthly = false

    } else if (selectedPeriod == 2) {
        reportPeriodDaily = false
        reportPeriodWeekly = true
        reportPeriodMonthly = false

    } else {
        reportPeriodDaily = false
        reportPeriodWeekly = false
        reportPeriodMonthly = true
    }

    var target by remember { mutableIntStateOf(0)}

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // header
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
                    navController.navigate(Screens.Home.route)
                },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.Black
                )
            ) {
                Icon(Icons.Default.Home, "Home")
            }

            Spacer(modifier = Modifier.weight(1f))

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

        Text(text = "Sleep Report", fontSize = 30.sp, color = Color(0xFF11435C))

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

        Spacer(modifier = Modifier.height(10.dp))

        // title
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Sleep",
                fontSize = 20.sp,
                color = Color(0xFF4F1E7E),
                fontWeight = FontWeight.Bold
            )
        }


        // graph
        Box(
            modifier = Modifier
                .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
                .fillMaxWidth()
                .height(150.dp)
                .border(2.dp, Color(0xF14B3283), shape = RoundedCornerShape(8.dp))
        ) {
            BarChart(
                modifier = Modifier
                    .height(150.dp)
                    .border(2.dp, Color(0xF14B3283), shape = RoundedCornerShape(8.dp)),
                barChartData = barChartSleep(
                    selectedPeriod,
                    auth
                )
            )
        }

        // Goal & Average

        selectedPeriod = if (reportPeriodDaily) 1 else if (reportPeriodWeekly) 2 else 3

        Row(
            modifier = Modifier
                .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
        ) {

            target = loadTargetAverage(selectedPeriod, navController, auth)
        }

        val avg = getAverage(auth)

        if (target != 0) {
            if (avg > target) {

                Text(
                    text = "You are above your goal",
                    fontSize = 13.sp,
                    color = Color(0xFF3AAD3C),
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .padding(start = 190.dp)
                )
            } else {
                Text(
                    text = "You are below your goal",
                    fontSize = 13.sp,
                    color = Color.Red,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .padding(start = 190.dp)
                )
            }
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.Start
        ){
            Text(
                text = "History",
                fontSize = 30.sp
            )

            Icon(
                painter = painterResource(id = R.drawable.history),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(40.dp)
                    .padding(start = 5.dp, top = 3.dp)
            )
        }

        ShowSleepList(auth)
    }
}

@Composable
private fun loadTargetAverage(
    selectedPeriod: Int,                    // 1 - Daily, 2 - Weekly, 3 - Monthly
    navController: NavController,
    auth: FirebaseAuth
): Int {

    val period = when (selectedPeriod) {
        1 -> "Daily"
        2 -> "Weekly"
        else -> "Monthly"
    }

    var target by remember { mutableIntStateOf(0) }

    DisposableEffect(auth) {
        val database = Firebase.database
        val userId = auth.currentUser?.uid

        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val sleepTarget = dataSnapshot.getValue(SleepTarget::class.java)
                    target = sleepTarget!!.timeTarget
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Error: ${databaseError.message}")
            }
        }

        userId?.let {
            database.getReference("users").child(it).child("SleepTarget")
                .addValueEventListener(listener)

            onDispose {
                // Remove the listener when the composable is disposed
                database.getReference("users").child(it).child("SleepTarget")
                    .removeEventListener(listener)
            }
        }!!
    }

    if (target != 0) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(                                // Show/Set goal
                modifier = Modifier
                    .background(Color(0xFFCCBCEE), shape = RoundedCornerShape(17.dp))
                    .border(2.dp, Color(0xF14B3283), shape = RoundedCornerShape(17.dp))
                    .weight(1f)
                    .height(48.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Your goal",
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(start = 16.dp, top = 16.dp)
                    )

                    Spacer(modifier = Modifier.width(40.dp))

                    val targetShow = if (target % 2 == 0) "${target/2}h" else "${(target/2)}" + "h30"
                    Text(
                        text = targetShow,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(top = 16.dp),
                        textAlign = TextAlign.End
                    )
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            Box(                                // Average
                modifier = Modifier
                    .background(Color(0xFFCCBCEE), shape = RoundedCornerShape(17.dp))
                    .border(2.dp, Color(0xF14B3283), shape = RoundedCornerShape(17.dp))
                    .weight(1f)
                    .height(48.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Average",
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(start = 16.dp, top = 15.dp, bottom = 10.dp)
                            .weight(1f)
                    )

                    Spacer(modifier = Modifier.width(30.dp))

                    val avg = getAverage(auth)
                    val avgShow = if (avg % 2 == 0) "${avg/2}h" else "${(avg/2)}" + "h30"
                    Text(
                        text = avgShow,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .weight(1f)
                    )
                }
            }
        }
    } else {
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFCCBCEE), shape = RoundedCornerShape(17.dp))
                    .border(2.dp, Color(0xF14B3283), shape = RoundedCornerShape(17.dp)),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Define your $period goal",
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(start = 20.dp, top = 14.dp)
                )



                TextButton(
                    onClick = {
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
                ) {
                    Text(
                        text = "Add",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )
                }
            }
        }
    }

    return target
}

@Composable
private fun getTarget(auth: FirebaseAuth): Int {

    var target by remember { mutableIntStateOf(0)}

    DisposableEffect(auth) {
        val database = Firebase.database
        val userId = auth.currentUser?.uid

        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val sleepTarget = dataSnapshot.getValue(SleepTarget::class.java)
                    target = sleepTarget!!.timeTarget
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Error: ${databaseError.message}")
            }
        }

        userId?.let {
            database.getReference("users").child(it).child("SleepTarget")
                .addValueEventListener(listener)

            onDispose {
                // Remove the listener when the composable is disposed
                database.getReference("users").child(it).child("SleepTarget")
                    .removeEventListener(listener)
            }
        }!!
    }
    return target
}

@Composable
private fun getAverage(auth: FirebaseAuth): Int {
    val database = com.google.firebase.Firebase.database
    val userId = auth.currentUser?.uid
    var sleepData: List<Sleep> by remember { mutableStateOf(emptyList()) }

    val sleepPath = database.getReference("users/$userId/Sleep")

    // Use LaunchedEffect to fetch data asynchronously
    LaunchedEffect(Unit) {
        sleepPath.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val dataList = mutableListOf<Sleep>()
                for (i in snapshot.children) {
                    val sleep = i.getValue(Sleep::class.java)
                    sleep?.let {
                        dataList.add(it)
                    }
                }
                sleepData += dataList
            }
        }
    }
    return calculateAverageSleepTime(sleepData)
}

private fun calculateAverageSleepTime(sleeps: List<Sleep>): Int {

    if (sleeps.isEmpty()) {
        return 0
    }
    val totalHourSlept = sleeps.sumOf { it.hourSlept}
    val totalMinSlept = sleeps.sumOf { it.minSlept}

    return (totalHourSlept + totalMinSlept) / sleeps.size
}

@Composable
private fun ShowSleepList(auth: FirebaseAuth) {
    val database = com.google.firebase.Firebase.database
    val userId = auth.currentUser?.uid
    var sleepData: List<Sleep> by remember { mutableStateOf(emptyList()) }

    val sleepPath = database.getReference("users/$userId/Sleep")

    // Use LaunchedEffect to fetch data asynchronously
    LaunchedEffect(Unit) {
        sleepPath.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val dataList = mutableListOf<Sleep>()
                for (i in snapshot.children) {
                    val sleep = i.getValue(Sleep::class.java)
                    sleep?.let {
                        dataList.add(it)
                    }
                }
                sleepData += dataList
            }
        }
    }

    // Display the data in a Column with dynamic Rows
    LazyColumn {
        items(sleepData.reversed()) { sleep ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(6.dp)
                    .background(
                        Color(0xFFCCBCEE),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(2.dp, Color(0xF14B3283), shape = RoundedCornerShape(12.dp))
                    .clickable { },
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = convertDate(sleep.date),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 15.sp
                        )
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val hourSlept = getHourDifference(sleep.bedTimeHour, sleep.bedTimeMin, sleep.awakeTimeHour, sleep.awakeTimeMin)
                    val minSlept = getMinDifference(sleep.bedTimeHour, sleep.bedTimeMin, sleep.awakeTimeHour, sleep.awakeTimeMin)
                    Text(
                        text = if (minSlept == 0) "${hourSlept}h" else "${hourSlept}h${minSlept}",
                        style = TextStyle(
                            fontSize = 15.sp
                        )
                    )
                    Text(
                        text =
                        if (sleep.bedTimeMin == 0 && sleep.awakeTimeMin != 0)
                            "${sleep.bedTimeHour}h00 - ${sleep.awakeTimeHour}h${sleep.awakeTimeMin}"
                        else if (sleep.awakeTimeMin == 0 && sleep.bedTimeMin != 0)
                            "${sleep.bedTimeHour}h${sleep.bedTimeMin} - ${sleep.awakeTimeHour}h00"
                        else if (sleep.bedTimeMin == 0)  // here awakeTimeMin has to be 0
                            "${sleep.bedTimeHour}h00 - ${sleep.awakeTimeHour}h00"
                        else
                            "${sleep.bedTimeHour}h${sleep.bedTimeMin} - ${sleep.awakeTimeHour}h${sleep.awakeTimeMin}",

                        style = TextStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        ),
                        modifier = Modifier
                            .alpha(0.5f)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val target = getTarget(auth)
                    val hourSlept = getHourDifference(sleep.bedTimeHour, sleep.bedTimeMin, sleep.awakeTimeHour, sleep.awakeTimeMin)
                    val minSlept = getMinDifference(sleep.bedTimeHour, sleep.bedTimeMin, sleep.awakeTimeHour, sleep.awakeTimeMin)

                    val targetAchieved = (if (minSlept == 30) (hourSlept*2)+1 else (hourSlept*2)) >= target

                    if (targetAchieved) {
                        Icon(
                            painter = painterResource(id = R.drawable.green_check_icon),
                            contentDescription = "targetAchievedCheck",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(20.dp)
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.red_x_icon),
                            contentDescription = "targetAchievedCheck",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(20.dp)
                        )
                    }

                    Text(
                        text = "Daily Goal",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 10.sp
                        ))
                }
            }
        }
    }
}

@RequiresApi(34)
@Composable
fun barChartSleep(
    selectedPeriod: Int,
    auth: FirebaseAuth
):BarChartData {
    val database = com.google.firebase.Firebase.database
    val userId = auth.currentUser?.uid
    var sleepData: List<Sleep> by remember { mutableStateOf(emptyList()) }

    val barChartListSize = if (selectedPeriod == 1) 7 else if (selectedPeriod == 2) 10 else 12
    val yStepSize = 5

    val sleepPath = database.getReference("users/$userId/Sleep")

    // Use LaunchedEffect to fetch data asynchronously
    LaunchedEffect(Unit) {
        sleepPath.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val dataList = mutableListOf<Sleep>()
                for (i in snapshot.children) {
                    val sleep = i.getValue(Sleep::class.java)
                    sleep?.let {
                        dataList.add(it)
                    }
                }
                sleepData += dataList
            }
        }
    }

    var maxRange = 0
    if (selectedPeriod == 1) {
        val splitByDay = splitSleepsByDay(sleepData)
        val size = splitByDay.size
        var control = 0
        for (i in 0 until barChartListSize) {
            if (control < size) {
                if (maxRange < sumTimeSlept(splitByDay[i])) {
                    maxRange = sumTimeSlept(splitByDay[i]).toInt()
                }
                control++
            }
        }
    } else if (selectedPeriod == 2) {
        val splitByWeek = splitSleepsByWeek(sleepData)
        val size = splitByWeek.size
        var control = 0
        for (i in 0 until barChartListSize) {
            if (control < size) {
                if (maxRange < sumTimeSlept(splitByWeek[i])) {
                    maxRange = sumTimeSlept(splitByWeek[i]).toInt()
                }
                control++
            }
        }
    } else {
        for (i in 0 until barChartListSize) {
            if (maxRange < sumTimeSleptMonth(sleepData, i)) {
                maxRange = sumTimeSleptMonth(sleepData, i).toInt()
            }
        }
    }

    maxRange = (maxRange*1.2).toInt()

    val barData = getBarChartDataUpdated(
        sleepData,
        barChartListSize,
        DataCategoryOptions(),
        selectedPeriod
    )

    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .steps(barData.size - 1)
        .backgroundColor(Color(0xFFCCBCEE))
        .bottomPadding(20.dp)
        .labelAndAxisLinePadding(8.dp)
        .axisLabelAngle(if (selectedPeriod == 1) 0f else 30f)
        .axisLabelFontSize(12.sp)
        .startDrawPadding(20.dp)
        .labelData { index -> barData[index].label }
        .build()

    val yAxisData = AxisData.Builder()
        .steps(yStepSize)
        .backgroundColor(Color(0xFFCCBCEE))
        .labelAndAxisLinePadding(10.dp)
        .axisOffset(20.dp)
        .labelData { index -> (index * ((maxRange / yStepSize))/2).toString() + "h" }
        .axisLabelDescription { _ -> "Hours (h)" }
        .build()

    return BarChartData(
        chartData = barData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        backgroundColor = Color(0xFFCCBCEE),
        barStyle = BarStyle(
            paddingBetweenBars = if (selectedPeriod == 1) 26.dp else if (selectedPeriod == 2) 16.dp else 13.dp,
            barWidth = if (selectedPeriod == 1) 21.dp else if (selectedPeriod == 2) 16.dp else 13.dp
        ),
        showYAxis = true,
        showXAxis = true,
        paddingEnd = 0.dp
    )
}

@RequiresApi(34)
@Composable
private fun getBarChartDataUpdated(
    sleeps: List<Sleep>,
    listSize: Int,
    dataCategoryOptions: DataCategoryOptions,
    selectedPeriod: Int
): List<BarData> {
    val list = arrayListOf<BarData>()
    val monthlyList = arrayListOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    if (selectedPeriod == 1) {                      // Sleep Daily
        val splitByDay = splitSleepsByDay(sleeps)
        val size = splitByDay.size
        var control = 0

        if (splitByDay.isNotEmpty()) {
            var dayOfWeek = LocalDate.ofInstant(Instant.ofEpochMilli(splitByDay[0][0].date), ZoneId.systemDefault()).dayOfWeek

            for (i in 0 until listSize) {
                if (control < size) {

                    val distanceString = "%.2f".format(sumTimeSlept(splitByDay[i])).replace(",", ".")
                    val point = Point(i.toFloat(), distanceString.toFloat())

                    list.add(
                        BarData(
                            point = point,
                            color = if (control == size-1 ) Color(0xFF4F1E7E) else Color(0xF14B3283),
                            dataCategoryOptions = dataCategoryOptions,
                            label = "$dayOfWeek".substring(0,3)
                        )
                    )
                    control++
                    dayOfWeek = dayOfWeek.plus(1)
                } else {
                    val point = Point(i.toFloat(), 0f)
                    list.add(
                        BarData(
                            point = point,
                            color = Color(0xF14B3283),
                            dataCategoryOptions = dataCategoryOptions,
                            label = "$dayOfWeek".substring(0,3)
                        )
                    )
                    dayOfWeek = dayOfWeek.plus(1)
                }
            }
        } else {
            var dayOfWeek = LocalDate.now().dayOfWeek
            for (i in 0 until listSize) {
                val point = Point(i.toFloat(), 0f)
                list.add(
                    BarData(
                        point = point,
                        color = Color(0xF14B3283),
                        dataCategoryOptions = dataCategoryOptions,
                        label = "$dayOfWeek".substring(0,3)
                    )
                )
                dayOfWeek = dayOfWeek.plus(1)
            }
        }
    } else if (selectedPeriod == 2) {              // Sleep Weekly
        val splitByWeek = splitSleepsByWeek(sleeps)
        val size = splitByWeek.size
        var control = 0

        if (splitByWeek.isNotEmpty()) {
            var dayAndMonth = LocalDate.ofInstant(Instant.ofEpochMilli(splitByWeek[0][0].date), ZoneId.systemDefault())
            val month = when (dayAndMonth.month) {
                Month.JANUARY -> "01"
                Month.FEBRUARY -> "02"
                Month.MARCH -> "03"
                Month.APRIL -> "04"
                Month.MAY -> "05"
                Month.JUNE -> "06"
                Month.JULY -> "07"
                Month.AUGUST -> "08"
                Month.SEPTEMBER -> "09"
                Month.OCTOBER -> "10"
                Month.NOVEMBER ->  "11"
                else ->  "12"
            }

            for (i in 0 until listSize) {
                if (control < size) {


                    val distanceString = "%.2f".format(sumTimeSlept(splitByWeek[i])).replace(",", ".")
                    val point = Point(i.toFloat(), distanceString.toFloat())

                    list.add(
                        BarData(
                            point = point,
                            color = if (control == size-1 ) Color(0xFF4F1E7E) else Color(0xF14B3283),
                            dataCategoryOptions = dataCategoryOptions,
                            label = "${dayAndMonth.dayOfMonth}/$month"
                        )
                    )
                    control++
                    dayAndMonth = dayAndMonth.plusDays(7)
                } else {
                    val point = Point(i.toFloat(), 0f)
                    list.add(
                        BarData(
                            point = point,
                            color = Color(0xF14B3283),
                            dataCategoryOptions = dataCategoryOptions,
                            label = "${dayAndMonth.dayOfMonth}/$month"
                        )
                    )
                    dayAndMonth = dayAndMonth.plusDays(7)
                }
            }
        } else {
            var dayAndMonth = LocalDate.now()
            val month = when (dayAndMonth.month) {
                Month.JANUARY -> "01"
                Month.FEBRUARY -> "02"
                Month.MARCH -> "03"
                Month.APRIL -> "04"
                Month.MAY -> "05"
                Month.JUNE -> "06"
                Month.JULY -> "07"
                Month.AUGUST -> "08"
                Month.SEPTEMBER -> "09"
                Month.OCTOBER -> "10"
                Month.NOVEMBER ->  "11"
                else ->  "12"
            }
            for (i in 0 until listSize) {
                val point = Point(i.toFloat(), 0f)
                list.add(
                    BarData(
                        point = point,
                        color = Color(0xF14B3283),
                        dataCategoryOptions = dataCategoryOptions,
                        label = "${dayAndMonth.dayOfMonth}/$month"
                    )
                )
                dayAndMonth = dayAndMonth.plusDays(7)
            }
        }
    } else {                                      // Sleep Monthly
        if (sleeps.isNotEmpty()) {
            val control = when (LocalDate.now().month) {
                Month.JANUARY -> 0
                Month.FEBRUARY -> 1
                Month.MARCH -> 2
                Month.APRIL -> 3
                Month.MAY -> 4
                Month.JUNE -> 5
                Month.JULY -> 6
                Month.AUGUST -> 7
                Month.SEPTEMBER -> 8
                Month.OCTOBER -> 9
                Month.NOVEMBER ->  10
                else ->  11
            }

            for (index in 0 until listSize) {

                val distanceString = "%.2f".format(sumTimeSleptMonth(sleeps, index)).replace(",", ".")
                val point = Point(index.toFloat(), distanceString.toFloat())

                list.add(
                    BarData(
                        point = point,
                        color = if (index == control) Color(0xFF4F1E7E) else Color(0xF14B3283),
                        dataCategoryOptions = dataCategoryOptions,
                        label = monthlyList[index]
                    )
                )
            }
        } else {
            for (index in 0 until listSize) {
                val point = Point(index.toFloat(), 0.toFloat())
                list.add(
                    BarData(
                        point = point,
                        color = Color(0xF14B3283),
                        dataCategoryOptions = dataCategoryOptions,
                        label = monthlyList[index]
                    )
                )
            }
        }
    }
    return list
}

private fun getHourDifference(hour1: Int, minute1: Int, hour2: Int, minute2: Int): Int {
    val totalMinutes1 = hour1 * 60 + minute1
    val totalMinutes2 = hour2 * 60 + minute2

    val differenceMinutes = (totalMinutes2 - totalMinutes1 + 24 * 60) % (24 * 60)

    return differenceMinutes / 60
}

private fun getMinDifference(hour1: Int, minute1: Int, hour2: Int, minute2: Int): Int {
    val totalMinutes1 = hour1 * 60 + minute1
    val totalMinutes2 = hour2 * 60 + minute2

    val differenceMinutes = (totalMinutes2 - totalMinutes1 + 24 * 60) % (24 * 60)

    return differenceMinutes % 60
}

private fun sumTimeSlept(sleeps: List<Sleep>): Double {
    var sum = 0
    for (sleep in sleeps) {
        val hourSlept = getHourDifference(sleep.bedTimeHour, sleep.bedTimeMin, sleep.awakeTimeHour, sleep.awakeTimeMin)
        val minSlept = getMinDifference(sleep.bedTimeHour, sleep.bedTimeMin, sleep.awakeTimeHour, sleep.awakeTimeMin)
        val timeSlept = (if (minSlept == 30) (hourSlept*2)+1 else (hourSlept*2))

        sum += timeSlept
    }
    return (sum/sleeps.size).toDouble()
}

@RequiresApi(34)
private fun splitSleepsByDay(sleeps: List<Sleep>): List<ArrayList<Sleep>> {
    // Initialize the result list
    val result = mutableListOf<ArrayList<Sleep>>()

    // Get the current date
    val currentDate = LocalDate.now()

    // Iterate through sleeps and split them by recent days
    var currentDayList = arrayListOf<Sleep>()
    var currentDay = LocalDate.MIN

    for (sleep in sleeps) {
        val sleepDate = LocalDate.ofInstant(Instant.ofEpochMilli(sleep.date), ZoneId.systemDefault())

        // Calculate the difference in days
        val daysDifference = ChronoUnit.DAYS.between(sleepDate, currentDate)

        if (sleepDate.isEqual(currentDay) && daysDifference <= 6) {
            // Add sleep to the current day within the recent 6 days
            currentDayList.add(sleep)
        } else if (sleepDate.isAfter(currentDay) && daysDifference <= 6) {
            // Start a new day within the recent 6 days
            if (currentDayList.isNotEmpty()) {
                result.add(currentDayList)
            }
            currentDayList = arrayListOf(sleep)
            currentDay = sleepDate
        }
    }

    // Add the last day if not empty
    if (currentDayList.isNotEmpty()) {
        result.add(currentDayList)
    }

    return result.takeLast(7)
}

@RequiresApi(34)
private fun splitSleepsByWeek(sleeps: List<Sleep>): List<ArrayList<Sleep>> {
    // Initialize the result list
    val result = mutableListOf<ArrayList<Sleep>>()

    // Get the current date
    val currentDate = LocalDate.now()

    // Iterate through sleeps and split them by recent weeks
    var currentWeekList = arrayListOf<Sleep>()
    var currentWeekStart = LocalDate.MIN

    for (sleep in sleeps) {
        val sleepDate = LocalDate.ofInstant(Instant.ofEpochMilli(sleep.date), ZoneId.systemDefault())

        // Calculate the difference in weeks
        val weeksDifference = ChronoUnit.WEEKS.between(sleepDate, currentDate)

        if (sleepDate.isAfter(currentWeekStart) &&
            sleepDate.dayOfWeek == DayOfWeek.MONDAY &&
            weeksDifference <= 9
        ) {
            // Start a new week within the recent 9 weeks
            if (currentWeekList.isNotEmpty()) {
                result.add(currentWeekList)
            }
            currentWeekList = arrayListOf(sleep)
            currentWeekStart = sleepDate
        } else if (weeksDifference <= 9) {
            // Add sleep to the current week within the recent 9 weeks
            currentWeekList.add(sleep)
        }
    }

    // Add the last week if not empty
    if (currentWeekList.isNotEmpty()) {
        result.add(currentWeekList)
    }

    return result.takeLast(10)
}

@RequiresApi(34)
private fun sumTimeSleptMonth(sleeps: List<Sleep>, monthIndex: Int): Double {
    // Get the current year
    val currentYear = LocalDate.now().year

    val month = when (monthIndex) {
        0 -> Month.JANUARY
        1 -> Month.FEBRUARY
        2 -> Month.MARCH
        3 -> Month.APRIL
        4 -> Month.MAY
        5 -> Month.JUNE
        6 -> Month.JULY
        7 -> Month.AUGUST
        8 -> Month.SEPTEMBER
        9 -> Month.OCTOBER
        10 -> Month.NOVEMBER
        else -> Month.DECEMBER
    }

    // Filter sleeps for the month specified of the current year
    val monthSleeps = sleeps.filter { sleep ->
        val sleepYear = LocalDate.ofInstant(Instant.ofEpochMilli(sleep.date), ZoneId.systemDefault()).year
        val sleepMonth = LocalDate.ofInstant(Instant.ofEpochMilli(sleep.date), ZoneId.systemDefault()).month
        (sleepYear == currentYear) && (sleepMonth == month)
    }

    var timeSlept = 0.0
    for (i in monthSleeps.indices) {
        if (monthSleeps[i].minSlept == 30) {
            timeSlept += monthSleeps[i].hourSlept + 0.5
        } else {
            timeSlept += monthSleeps[i].hourSlept
        }
    }
    return timeSlept
}

private fun convertDate(date: Long): String {
    val instant = Instant.ofEpochMilli(date)
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return localDateTime.format(formatter)
}