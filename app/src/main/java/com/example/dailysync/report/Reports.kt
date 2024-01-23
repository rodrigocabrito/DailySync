package com.example.dailysync.report

import android.util.Log
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
import com.example.dailysync.Exercise
import com.example.dailysync.FirebaseExerciseDataManager
import com.example.dailysync.FirebaseSleepDataManager
import com.example.dailysync.R
import com.example.dailysync.Sleep
import com.example.dailysync.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.random.Random

@RequiresApi(34)
@Composable
fun Reports(navController: NavController, auth: FirebaseAuth) {

    var reportPeriodDaily by remember { mutableStateOf(true) }
    var reportPeriodWeekly by remember { mutableStateOf(false) }
    var reportPeriodMonthly by remember { mutableStateOf(false) }

    var selectedExercise by remember { mutableIntStateOf(1)}            // 1 = Run, 2 = Walk, 3 = Cycle
    var selectedPeriod by remember { mutableIntStateOf(1)}            // 1 = Daily, 2 = Weekly, 3 = Monthly

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
        Text(text = "Your $title Report", fontSize = 20.sp)

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
                modifier = Modifier.weight(0.3f),
                color = Color(0xFF154E1C),
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = {
                    val route = Screens.ExerciseReport.route
                        .replace(
                            oldValue = "{selectedExercise}",
                            newValue = selectedExercise.toString()
                        )
                        .replace(
                            oldValue = "{selectedPeriod}",
                            newValue = selectedPeriod.toString()
                        )

                    Log.e("Route", route.toString())
                    navController.navigate(
                        route
                    )
                },
                modifier = Modifier
                    .height(25.dp)
                    .weight(0.2f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_error),
                    contentDescription = null,
                    tint = Color(0xFF154E1C),
                    modifier = Modifier
                        .size(35.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

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
                .border(2.dp, Color(0xFF1A8B47), shape = RoundedCornerShape(8.dp)),
        ) {
            BarChart(
                modifier = Modifier
                    .height(150.dp)
                    .border(2.dp, Color(0xFF1A8B47), shape = RoundedCornerShape(8.dp)),
                barChartData = barChart(1, selectedExercise, reportPeriodDaily, reportPeriodWeekly, auth))
        }

        /*
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
            BarChart(
                modifier = Modifier
                    .height(150.dp),
                barChartData = barChart(2, selectedExercise, reportPeriodDaily, reportPeriodWeekly, auth))
        }

        Text(
            text = "Read",
            fontSize = 20.sp,
            color = Color(0xFF64610F),
            fontWeight = FontWeight.Bold
        )*/

        /*
        Box(
            modifier = Modifier
                .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
                .fillMaxWidth()
                .clickable {
                    navController.navigate(Screens.ReadReport.route)            // TODO get args?
                }
                .border(2.dp, Color(0xFF91641F), shape = RoundedCornerShape(8.dp)),
        ) {
            BarChart(
                modifier = Modifier
                    .height(150.dp),
                barChartData = barChart(3, selectedExercise, reportPeriodDaily, reportPeriodWeekly, auth))
        }*/

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
                    .background(Color(0xFFA2D6F0))
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
                    .background(Color(0xFF2C8CBC))
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
                    .background(Color(0xFFA2D6F0))
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
                    .background(Color(0xFFA2D6F0))
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

// TODO receive the ordered list
@RequiresApi(34)
fun getBarChartDataUpdated(
    listOfRuns: List<Exercise>,
    listOfWalks: List<Exercise>,
    listOfCycles: List<Exercise>,
    listOfSleeps: List<Sleep>,
    //listOfReads: List<ReadingSession>,
    listSize: Int,
    maxRange: Int,
    dataCategoryOptions: DataCategoryOptions,
    activity: Int,
    selectedExercise: Int,
    reportPeriodDaily: Boolean,
    reportPeriodWeekly: Boolean
): List<BarData> {
    val list = arrayListOf<BarData>()
    val monthlyList = arrayListOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    //Log.e("Recebida - List Of Runs", listOfRuns.toString())
    //Log.e("Recebida - List Of Walks", listOfWalks.toString())
    //Log.e("Recebida - List Of Cycles", listOfCycles.toString())
    //Log.e("List Of Runs", listOfSleeps.toString())

    if (activity == 1) {
      if (reportPeriodDaily) {                      // Run Daily

          val splitByDay = if (selectedExercise == 1) splitExercisesByDay(listOfRuns) else if (selectedExercise == 2) splitExercisesByDay(listOfWalks) else splitExercisesByDay(listOfCycles)
          if (splitByDay.isNotEmpty()) {
              //Log.e("Not empty run daily", "idk")
              for (i in 0 until listSize) {

                  val dayOfWeek = LocalDate.ofInstant(Instant.ofEpochMilli(splitByDay[i][0].date), ZoneId.systemDefault()).dayOfWeek
                  val point =
                      Point(i.toFloat(), "%.2f".format(sumDistances(splitByDay[i])).toFloat())

                  list.add(
                      BarData(
                          point = point,
                          color = Color(0xFF1A8B47),
                          dataCategoryOptions = dataCategoryOptions,
                          label = "$dayOfWeek"
                      )
                  )
              }
          } else {
              //Log.e(" empty run daily", "idk")
              for (i in 0 until listSize) {
                  val point = Point(i.toFloat(), 0.toFloat())
                  list.add(
                      BarData(
                          point = point,
                          color = Color(0xF14B3283),
                          dataCategoryOptions = dataCategoryOptions,
                          label = i.toString()
                      )
                  )
              }
              //Log.e("Fim do getBarChartDataUpdated", "barras do Run postas a 0")
          }
      } else if (reportPeriodWeekly) {              // Run Weekly

          val splitByWeek = if (selectedExercise == 1) splitExercisesByWeek(listOfRuns) else if (selectedExercise == 2) splitExercisesByWeek(listOfWalks) else splitExercisesByWeek(listOfCycles)
          if (splitByWeek.isNotEmpty()) {
              //Log.e("Not empty run weekly", "idk")
              for (i in 0 until listSize) {

                  val exerciseDay =
                      LocalDate.ofInstant(Instant.ofEpochMilli(splitByWeek[i][0].date), ZoneId.systemDefault()).dayOfMonth
                  val exerciseMonth =
                      LocalDate.ofInstant(Instant.ofEpochMilli(splitByWeek[i][0].date), ZoneId.systemDefault()).month

                  val point =
                      Point(i.toFloat(), "%.2f".format(sumDistances(splitByWeek[i])).toFloat())

                  list.add(
                      BarData(
                          point = point,
                          color = Color(0xFF1A8B47),
                          dataCategoryOptions = dataCategoryOptions,
                          label = "$exerciseDay/$exerciseMonth"
                      )
                  )
              }
          } else {
              //Log.e(" empty run weekly", "idk")
              for (i in 0 until listSize) {
                  val point = Point(i.toFloat(), 0.toFloat())
                  list.add(
                      BarData(
                          point = point,
                          color = Color(0xF14B3283),
                          dataCategoryOptions = dataCategoryOptions,
                          label = i.toString()
                      )
                  )
              }
          }
      } else {                                      // Run Monthly
          val orderedListOfExercises = if (selectedExercise == 1) listOfRuns else if (selectedExercise == 2) listOfWalks else listOfCycles

          if (orderedListOfExercises.isNotEmpty()) {
              //Log.e("Not empty run montly", "idk")
              for (index in 0 until listSize) {

                  val point = Point(
                      index.toFloat(),
                      "%.2f".format(sumDistancesMonth(orderedListOfExercises, index)).toFloat()
                  )

                  list.add(
                      BarData(
                          point = point,
                          color = Color(0xFF1A8B47),
                          dataCategoryOptions = dataCategoryOptions,
                          label = monthlyList[index]
                      )
                  )
              }
          } else {
              //Log.e(" empty run montly", "idk")
              for (i in 0 until listSize) {
                  val point = Point(i.toFloat(), 0.toFloat())
                  list.add(
                      BarData(
                          point = point,
                          color = Color(0xF14B3283),
                          dataCategoryOptions = dataCategoryOptions,
                          label = i.toString()
                      )
                  )
              }
          }
      }
    } else if (activity == 2) {                     // Sleep
        if (reportPeriodDaily) {                            // Sleep Daily

            val splitSleepsByDay = splitSleepsByDay(listOfSleeps)
            if (splitSleepsByDay.isNotEmpty()) {
                //Log.e("Not empty sleep daily", "idk")

                for (i in 0 until listSize) {

                    val dayOfWeek = LocalDate.ofInstant(Instant.ofEpochMilli(splitSleepsByDay[i][0].date), ZoneId.systemDefault()).dayOfWeek

                    val point = Point(i.toFloat(), "%.2f".format(sumTimeSlept(splitSleepsByDay[i])).toFloat())

                    list.add(
                        BarData(
                            point = point,
                            color = Color(0xF14B3283),
                            dataCategoryOptions = dataCategoryOptions,
                            label = "$dayOfWeek"
                        )
                    )
                }
            } else {
                //Log.e(" empty sleep daily", "idk")
                for (i in 0 until listSize) {
                    val point = Point(i.toFloat(), 0.toFloat())
                    list.add(
                        BarData(
                            point = point,
                            color = Color(0xF14B3283),
                            dataCategoryOptions = dataCategoryOptions,
                            label = i.toString()
                        )
                    )
                }
            }
        } else if (reportPeriodWeekly) {            // Sleep Weekly

            val splitSleepsByWeek = splitSleepsByWeek(listOfSleeps)
            if (splitSleepsByWeek.isNotEmpty()) {
                //Log.e("Not empty sleep weekly", "idk")
                for (i in 0 until listSize) {

                    val sleepDay = LocalDate.ofInstant(Instant.ofEpochMilli(splitSleepsByWeek[i][0].date), ZoneId.systemDefault()).dayOfMonth
                    val sleepMonth = LocalDate.ofInstant(Instant.ofEpochMilli(splitSleepsByWeek[i][0].date), ZoneId.systemDefault()).month

                    val point = Point(
                        i.toFloat(),
                        "%.2f".format(Random.nextDouble(1.0, maxRange.toDouble())).toFloat()
                    )

                    list.add(
                        BarData(
                            point = point,
                            color = Color(0xF14B3283),
                            dataCategoryOptions = dataCategoryOptions,
                            label = "$sleepDay/$sleepMonth"
                        )
                    )
                }
            } else {
                //Log.e(" empty sleep weekly", "idk")
                for (i in 0 until listSize) {
                    val point = Point(i.toFloat(), 0.toFloat())
                    list.add(
                        BarData(
                            point = point,
                            color = Color(0xF14B3283),
                            dataCategoryOptions = dataCategoryOptions,
                            label = i.toString()
                        )
                    )
                }
            }
        } else {                                    // Sleep Monthly
            if (listOfSleeps.isNotEmpty()) {
                //Log.e("Not empty sleep monthly", "idk")
                for (i in 0 until listSize) {

                    val point = Point(i.toFloat(), "%.2f".format(sumTimeSleptMonth(listOfSleeps, i)).toFloat())

                    list.add(
                        BarData(
                            point = point,
                            color = Color(0xF14B3283),
                            dataCategoryOptions = dataCategoryOptions,
                            label = monthlyList[i]
                        )
                    )
                }
            } else {
                //Log.e(" empty sleep monthly", "idk")
                for (i in 0 until listSize) {
                    val point = Point(i.toFloat(), 0.toFloat())
                    list.add(
                        BarData(
                            point = point,
                            color = Color(0xF14B3283),
                            dataCategoryOptions = dataCategoryOptions,
                            label = i.toString()
                        )
                    )
                }
            }
        }
    } else {                                        // Read// TODO ALL
        // TODO Read
        if (reportPeriodDaily) {

        } else if (reportPeriodWeekly) {

        } else {

        }
    }

    //Log.e("Lista de Barras", list.toString())
    return list
}

@RequiresApi(34)
fun barChart(
    activity: Int,
    selectedExercise: Int,
    reportPeriodDaily: Boolean,
    reportPeriodWeekly: Boolean,
    auth: FirebaseAuth
): BarChartData {

    val barChartListSize = if (reportPeriodDaily) 7 else if (reportPeriodWeekly) 10 else 12
    var maxRange = 0

    val userId = auth.currentUser?.uid

    val listOfRuns = arrayListOf<Exercise>()
    val listOfWalks = arrayListOf<Exercise>()
    val listOfCycles = arrayListOf<Exercise>()
    val listOfSleeps = arrayListOf<Sleep>()
    //val listOfReads = arrayListOf<ReadingSession>()

    if (activity == 1) {
        if (selectedExercise == 1) {    // Run

            val firebaseDataManager = FirebaseExerciseDataManager()
            firebaseDataManager.getExercises(userId.toString(), selectedExercise) { runs ->
                for (run in runs) {
                    if (run.distance > maxRange)
                        maxRange = run.distance.toInt()

                    listOfRuns.add(run)
                }
                //Log.e("List of Runs no fim do ciclo", listOfRuns.toString())
            }

        } else if (selectedExercise == 2) {     // Walk
            val firebaseDataManager = FirebaseExerciseDataManager()
            firebaseDataManager.getExercises(userId.toString(), selectedExercise) { walks ->
                for (walk in walks) {
                    if (walk.distance > maxRange)
                        maxRange = walk.distance.toInt()

                    listOfWalks.add(walk)
                }
                //Log.e("List of Walks no fim do ciclo", listOfWalks.toString())
            }

        } else {    // Cycle
            val firebaseDataManager = FirebaseExerciseDataManager()
            firebaseDataManager.getExercises(userId.toString(), selectedExercise) { cycles ->
                for (cycle in cycles) {
                    if (cycle.distance > maxRange)
                        maxRange = cycle.distance.toInt()

                    listOfCycles.add(cycle)
                }
                //Log.e("List of Cycles no fim do ciclo", listOfCycles.toString())
            }
        }
    } else if (activity == 2) {     // Sleep
        val firebaseDataManager = FirebaseSleepDataManager()
        //Log.e("Antes do get Sleeps", "Antes")
        firebaseDataManager.getSleeps(userId.toString()) { sleeps ->
            for (sleep in sleeps) {
                val sleepTime = if (sleep.minSlept == 30) sleep.hourSlept + 1 else sleep.hourSlept
                if (sleepTime > maxRange)
                    maxRange = sleepTime

                listOfSleeps.add(sleep)
            }
            //Log.e("List of Sleeps no fim do ciclo", listOfSleeps.toString())
        }

    } else {    // Read
        // TODO read
    }

    //Log.e("List of Runs", listOfRuns.toString())
    val orderedListOfRuns = orderList(listOfRuns)

    //Log.e("List of ordered Runs", orderedListOfRuns.toString())
    val orderedListOfWalks = orderList(listOfWalks)
    val orderedListOfCycles = orderList(listOfCycles)
    val orderedListOfSleeps = orderList(listOfSleeps)
    //val orderedListOfReads = orderList(listOfReads)

    //og.e("orderedListOfRuns", orderedListOfRuns.toString())

    val yStepSize = 5    // should be 0              // TODO based of max value and type of activity (kms/hours/pages)

    if (activity == 1) {                                        // Exercise
        maxRange = (maxRange + (maxRange*0.15)).toInt()
    } else if (activity == 2) {                                 // Sleep
        maxRange++
    } else {                                                    // Read
        maxRange = (maxRange + (maxRange*0.2)).toInt()
    }

    val barData = getBarChartDataUpdated(
        listOfRuns,
        listOfWalks,
        listOfCycles,
        listOfSleeps,
        //listOfReads,
        barChartListSize,
        maxRange,
        DataCategoryOptions(),
        activity,
        selectedExercise,
        reportPeriodDaily,
        reportPeriodWeekly
    )

    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .steps(barData.size - 1)
        .backgroundColor(if (activity == 1) Color(0xFFA2F0C1) else if (activity == 2) Color(0xFFA17FEB) else Color(0xFFE5AE5A))
        .bottomPadding(20.dp)
        .labelAndAxisLinePadding(8.dp)
        .axisLabelAngle(if (reportPeriodDaily) 0f else if (reportPeriodWeekly) 0f else 30f)
        .axisLabelFontSize(10.sp)
        .startDrawPadding(20.dp)
        .labelData { index -> barData[index].label }
        .build()

    val yAxisData = AxisData.Builder()
        .steps(yStepSize)
        .backgroundColor(if (activity == 1) Color(0xFFA2F0C1) else if (activity == 2) Color(0xFFA17FEB) else Color(0xFFE5AE5A))
        .labelAndAxisLinePadding(10.dp)
        .axisOffset(20.dp)
        .labelData { index -> (index * (maxRange / yStepSize)).toString() }
        .build()

    return BarChartData(
        chartData = barData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        backgroundColor = if (activity == 1) Color(0xFFA2F0C1) else if (activity == 2) Color(0xFFA17FEB) else Color(0xFFE5AE5A),
        barStyle = BarStyle(
            paddingBetweenBars = if (reportPeriodDaily) 26.dp else if (reportPeriodWeekly) 16.dp else 13.dp,
            barWidth = if (reportPeriodDaily) 21.dp else if (reportPeriodWeekly) 16.dp else 13.dp
        ),
        showYAxis = true,
        showXAxis = true,
        paddingEnd = 0.dp
    )
}

// Generic function that orders ArrayList of any type by date
inline fun <reified T : Any> orderList(
    inputList: ArrayList<T>
): ArrayList<T> {

    val orderedList = ArrayList<T>()

    // Order the input list by the 'date' attribute using the sortedBy function
    val sortedList = inputList.sortedBy { item ->
        when (item) {
            is Exercise -> item.date
            is Sleep -> item.date
            //is ReadingSession -> item.date                  // TODO
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

    orderedList.addAll(sortedList)

    return orderedList
}

@RequiresApi(34)
fun sumDistancesMonth(exercises: List<Exercise>, monthIndex: Int): Double {
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

    // Filter exercises for the month specified of the current year
    val monthExercises = exercises.filter { exercise ->
        val exerciseYear = LocalDate.ofInstant(Instant.ofEpochMilli(exercise.date), ZoneId.systemDefault()).year
        val exerciseMonth = LocalDate.ofInstant(Instant.ofEpochMilli(exercise.date), ZoneId.systemDefault()).month
        (exerciseYear == currentYear) && (exerciseMonth == month)
    }

    // Sum up the distances of exercises in January
    return monthExercises.sumOf { it.distance.toDouble() }
}

@RequiresApi(34)
fun sumTimeSleptMonth(sleeps: List<Sleep>, monthIndex: Int): Double {
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

    // Filter exercises for the month specified of the current year
    val monthSleeps = sleeps.filter { sleep ->
        val exerciseYear = LocalDate.ofInstant(Instant.ofEpochMilli(sleep.date), ZoneId.systemDefault()).year
        val exerciseMonth = LocalDate.ofInstant(Instant.ofEpochMilli(sleep.date), ZoneId.systemDefault()).month
        (exerciseYear == currentYear) && (exerciseMonth == month)
    }

    var timeSlept = 0.0
    for (i in sleeps.indices) {
        if (monthSleeps[i].minSlept == 30) {
            timeSlept += monthSleeps[i].hourSlept + 0.5
        } else {
            timeSlept += monthSleeps[i].hourSlept
        }
    }
    return timeSlept
}

@RequiresApi(34)
fun splitExercisesByWeek(exercises: List<Exercise>): List<ArrayList<Exercise>> {
    // Initialize the result list
    val result = mutableListOf<ArrayList<Exercise>>()

    // Get the current date
    val currentDate = LocalDate.now()

    // Iterate through exercises and split them by recent weeks
    var currentWeekList = arrayListOf<Exercise>()
    var currentWeekStart = LocalDate.MIN

    for (exercise in exercises) {
        val exerciseDate = LocalDate.ofInstant(Instant.ofEpochMilli(exercise.date), ZoneId.systemDefault())

        // Calculate the difference in weeks
        val weeksDifference = ChronoUnit.WEEKS.between(exerciseDate, currentDate)

        if (exerciseDate.isAfter(currentWeekStart) &&
            exerciseDate.dayOfWeek == DayOfWeek.MONDAY &&
            weeksDifference <= 9
        ) {
            // Start a new week within the recent 9 weeks
            if (currentWeekList.isNotEmpty()) {
                result.add(currentWeekList)
            }
            currentWeekList = arrayListOf(exercise)
            currentWeekStart = exerciseDate
        } else if (weeksDifference <= 9) {
            // Add exercise to the current week within the recent 9 weeks
            currentWeekList.add(exercise)
        }
    }

    // Add the last week if not empty
    if (currentWeekList.isNotEmpty()) {
        result.add(currentWeekList)
    }

    return result.takeLast(10)
}

@RequiresApi(34)
fun splitSleepsByWeek(sleeps: List<Sleep>): List<ArrayList<Sleep>> {
    // Initialize the result list
    val result = mutableListOf<ArrayList<Sleep>>()

    // Get the current date
    val currentDate = LocalDate.now()

    // Iterate through exercises and split them by recent weeks
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
            // Add exercise to the current week within the recent 9 weeks
            currentWeekList.add(sleep)
        }
    }

    // Add the last week if not empty
    if (currentWeekList.isNotEmpty()) {
        result.add(currentWeekList)
    }

    return result.takeLast(10)
}

fun sumDistances(exercises: ArrayList<Exercise>) : Double {
    return exercises.sumOf { it.distance.toDouble() }
}

@RequiresApi(34)
fun splitExercisesByDay(exercises: List<Exercise>): List<ArrayList<Exercise>> {
    // Initialize the result list
    val result = mutableListOf<ArrayList<Exercise>>()
    // Get the current date
    val currentDate = LocalDate.now()

    // Iterate through exercises and split them by recent days
    var currentDayList = arrayListOf<Exercise>()
    var currentDay = LocalDate.MIN

    for (exercise in exercises) {
        val exerciseDate = LocalDate.ofInstant(Instant.ofEpochMilli(exercise.date), ZoneId.systemDefault())

        // Calculate the difference in days
        val daysDifference = ChronoUnit.DAYS.between(exerciseDate, currentDate)

        if (exerciseDate.isEqual(currentDay) && daysDifference <= 6) {
            // Add exercise to the current day within the recent 6 days
            currentDayList.add(exercise)
        } else if (exerciseDate.isAfter(currentDay) && daysDifference <= 6) {
            // Start a new day within the recent 6 days
            if (currentDayList.isNotEmpty()) {
                result.add(currentDayList)
            }
            currentDayList = arrayListOf(exercise)
            currentDay = exerciseDate
        }
    }

    // Add the last day if not empty
    if (currentDayList.isNotEmpty()) {
        result.add(currentDayList)
    }

    return result.takeLast(7)
}

@RequiresApi(34)
fun splitSleepsByDay(sleeps: List<Sleep>): List<ArrayList<Sleep>> {
    // Initialize the result list
    val result = mutableListOf<ArrayList<Sleep>>()

    // Get the current date
    val currentDate = LocalDate.now()

    // Iterate through exercises and split them by recent days
    var currentDayList = arrayListOf<Sleep>()
    var currentDay = LocalDate.MIN

    for (sleep in sleeps) {
        val sleepDate = LocalDate.ofInstant(Instant.ofEpochMilli(sleep.date), ZoneId.systemDefault())

        // Calculate the difference in days
        val daysDifference = ChronoUnit.DAYS.between(sleepDate, currentDate)

        if (sleepDate.isEqual(currentDay) && daysDifference <= 6) {
            // Add exercise to the current day within the recent 6 days
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

fun sumTimeSlept(sleeps: List<Sleep>) : Double {
    var timeSlept = 0.0
    for (i in sleeps.indices) {
        if (sleeps[i].minSlept == 30) {
            timeSlept += sleeps[i].hourSlept + 0.5
        } else {
            timeSlept += sleeps[i].hourSlept
        }
    }
    return timeSlept
}