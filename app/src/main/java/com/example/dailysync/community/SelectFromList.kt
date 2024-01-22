package com.example.dailysync.community

import android.os.Debug
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableTarget
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dailysync.Exercise
import com.example.dailysync.FirebaseExerciseDataManager
import com.example.dailysync.R
import com.example.dailysync.Sleep
import com.example.dailysync.bookModels.ReadingSession
import com.example.dailysync.navigation.Screens
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.database
import java.lang.Integer.parseInt

@Composable
fun SelectFromList(navController: NavController, auth: FirebaseAuth, type: String) {
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

        Text(text = type)

        Spacer(modifier = Modifier.height(20.dp))

        // list
        if (type == "Exercise") {
            ShowExerciseList(auth = auth)
        } else if (type == "Sleep") {
            ShowSleepList(auth = auth)
        } else if(type == "Read") {
            ShowReadingList(auth = auth)
        }


        // footer
        Spacer(modifier = Modifier.weight(1f))
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
                    .background(Color(android.graphics.Color.parseColor("#2C8CBC")))
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


@Composable
fun ShowExerciseList(auth: FirebaseAuth) {
    val database = Firebase.database
    val userId = auth.currentUser?.uid
    val exerciseWalkPath = database.getReference("users/$userId/Walk")
    val exerciseRunPath = database.getReference("users/$userId/Run")
    val exerciseCyclePath = database.getReference("users/$userId/Cycle")
    var exerciseData: List<Exercise> by remember { mutableStateOf(emptyList()) }

    // Use LaunchedEffect to fetch data asynchronously
    LaunchedEffect(Unit) {
        exerciseWalkPath.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val dataList = mutableListOf<Exercise>()
                for (i in snapshot.children) {
                    val exerciseWalk = i.getValue(Exercise::class.java)
                    exerciseWalk?.let {
                        it.type = "Walk"
                        dataList.add(it)
                    }
                }
                exerciseData += dataList
            }
        }
        exerciseRunPath.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val dataList = mutableListOf<Exercise>()
                for (i in snapshot.children) {
                    val exerciseRun = i.getValue(Exercise::class.java)
                    exerciseRun?.let {
                        it.type = "Run"
                        dataList.add(it)
                    }
                }
                exerciseData += dataList
            }
        }
        exerciseCyclePath.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val dataList = mutableListOf<Exercise>()
                for (i in snapshot.children) {
                    val exerciseCycle = i.getValue(Exercise::class.java)
                    exerciseCycle?.let {
                        it.type = "Cycle"
                        dataList.add(it)
                    }
                }
                exerciseData += dataList
            }
        }
    }

    // Display the data in a Column with dynamic Rows
    Column {
        for (exercise in exerciseData) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(8.dp)
                    .background(
                        Color(android.graphics.Color.parseColor("#A2F0C1")),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        /*navController.navigate(Screens.SelectFromList.route
                            .replace(oldValue = "{type}", newValue = "Exercise")
                        )*/
                        Log.d("Teste", "Clicou")
                    }
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp)),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${exercise.type}",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        )
                        if(exercise.type == "Walk"){
                            Image(
                                painter = painterResource(id = R.drawable.walk_icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp)
                            )
                        }
                        else if(exercise.type == "Run"){
                            Image(
                                painter = painterResource(id = R.drawable.run_icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp)
                            )
                        }
                        else{
                            Image(
                                painter = painterResource(id = R.drawable.cycle_icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(start = 4.dp)
                            )
                        }
                    }
                    Text(
                        text = "${exercise.date}",
                        style = TextStyle(
                            fontSize = 10.sp
                        )
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Distance",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    )
                    Text(
                        text = "${exercise.distance}",
                        style = TextStyle(
                            fontSize = 15.sp
                        )
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Time",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    )
                    Text(
                        text = "${exercise.time}",
                        style = TextStyle(
                            fontSize = 15.sp
                        )
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Rhythm",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    )
                    Text(
                        text = "${exercise.averagePace}",
                        style = TextStyle(
                            fontSize = 15.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ShowSleepList(auth: FirebaseAuth) {
    val database = Firebase.database
    val userId = auth.currentUser?.uid
    val sleepPath = database.getReference("users/$userId/Sleep")
    var sleepData: List<Sleep> by remember { mutableStateOf(emptyList()) }

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
    Column {
        for (sleep in sleepData) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(8.dp)
                    .background(
                        Color(android.graphics.Color.parseColor("#CCBCEE")),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        /*navController.navigate(Screens.SelectFromList.route
                            .replace(oldValue = "{type}", newValue = "Exercise")
                        )*/
                        Log.d("Teste", "Clicou")
                    }
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp)),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${sleep.date}",
                        style = TextStyle(
                            fontSize = 15.sp
                        )
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Bed Time",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    )
                    Text(
                        text =
                        if(sleep.bedTimeHour<10 && sleep.bedTimeMin<10){
                            "0${sleep.bedTimeHour}:0${sleep.bedTimeMin}"
                        }else if(sleep.bedTimeHour<10 && sleep.bedTimeMin>=10){
                            "0${sleep.bedTimeHour}:${sleep.bedTimeMin}"
                        }else if(sleep.bedTimeHour>=10 && sleep.bedTimeMin<10){
                            "${sleep.bedTimeHour}:0${sleep.bedTimeMin}"
                        }else{
                            "${sleep.bedTimeHour}:${sleep.bedTimeMin}"
                        },
                        style = TextStyle(
                            fontSize = 15.sp
                        )
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Wake Time",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    )
                    Text(
                        text =
                        if(sleep.awakeTimeHour<10 && sleep.awakeTimeMin<10){
                            "0${sleep.awakeTimeHour}:0${sleep.awakeTimeMin}"
                        }else if(sleep.awakeTimeHour<10 && sleep.awakeTimeMin>=10){
                            "0${sleep.awakeTimeHour}:${sleep.awakeTimeMin}"
                        }else if(sleep.awakeTimeHour>=10 && sleep.awakeTimeMin<10){
                            "${sleep.awakeTimeHour}:0${sleep.awakeTimeMin}"
                        }else{
                            "${sleep.awakeTimeHour}:${sleep.awakeTimeMin}"
                        },
                        style = TextStyle(
                            fontSize = 15.sp
                        )
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Sleep Time",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    )
                    Text(
                        text =
                        if(sleep.hourSlept<10 && sleep.minSlept<10){
                            "0${sleep.hourSlept}:0${sleep.minSlept}"
                        }else if(sleep.hourSlept<10 && sleep.minSlept>=10){
                            "0${sleep.hourSlept}:${sleep.minSlept}"
                        }else if(sleep.hourSlept>=10 && sleep.minSlept<10){
                            "${sleep.hourSlept}:0${sleep.minSlept}"
                        }else{
                             "${sleep.hourSlept}:${sleep.minSlept}"
                        },
                        style = TextStyle(
                            fontSize = 15.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ShowReadingList(auth: FirebaseAuth) {
    val database = Firebase.database
    val userId = auth.currentUser?.uid
    val readingPath = database.getReference("users/$userId/books/readingSessions")
    var readingData: List<ReadingSession> by remember { mutableStateOf(emptyList()) }

    // Use LaunchedEffect to fetch data asynchronously
    LaunchedEffect(Unit) {
        readingPath.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val dataList = mutableListOf<ReadingSession>()
                for (i in snapshot.children) {
                    val reading = i.getValue(ReadingSession::class.java)
                    reading?.let {
                        dataList.add(it)
                        Log.d("Teste", it.toString())
                    }
                }
                readingData += dataList
            }
        }
    }

    // Display the data in a Column with dynamic Rows
    Column {
        for (reading in readingData) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(8.dp)
                    .background(
                        Color(android.graphics.Color.parseColor("#F5D4A2")),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        /*navController.navigate(Screens.SelectFromList.route
                            .replace(oldValue = "{type}", newValue = "Exercise")
                        )*/
                        Log.d("Teste", "Clicou")
                    }
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp)),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    /*Text(
                        text = "${sleep.date}",
                        style = TextStyle(
                            fontSize = 15.sp
                        )
                    )*/
                }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Pages Read",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        )
                        Text(
                            text = "${reading.pagesRead}",
                            style = TextStyle(
                                fontSize = 15.sp
                            )
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "time Spent",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        )
                        Text(
                            text = "${reading.durationMinutes}",
                            style = TextStyle(
                                fontSize = 15.sp
                            )
                        )
                    }

                }
            }
        }
    }