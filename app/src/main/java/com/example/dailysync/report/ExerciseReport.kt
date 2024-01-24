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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
import com.example.dailysync.Exercise
import com.example.dailysync.R
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
import java.util.Locale


@RequiresApi(34)
@Composable
fun ExerciseReport(navController: NavController, selectedExerciseShow: Int, selectedPeriodShow: Int, auth: FirebaseAuth) {

    var reportPeriodDaily by remember { mutableStateOf(false) }
    var reportPeriodWeekly by remember { mutableStateOf(false) }
    var reportPeriodMonthly by remember { mutableStateOf(false) }

    val selectedExercise by remember { mutableIntStateOf(selectedExerciseShow) }            // 1 = Run, 2 = Walk, 3 = Cycle
    var selectedPeriod by remember { mutableIntStateOf(selectedPeriodShow) }                // 1 = Daily, 2 = Weekly, 3 = Monthly

    var editGoalPopUpVisible by remember { mutableStateOf(false) }
    var showConfirmationSavedPopUp by remember { mutableStateOf(false) }

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

    val titleChart = when (selectedExercise) {
        1 -> "Run"
        2 -> "Walk"
        else -> "Cycle"
    }

    var goal by remember { mutableDoubleStateOf(0.0)}

    // Press OK (Pop Up)
    val confirmEditAction: (String, String) -> Unit = { goalPath, textFieldValue ->
        editGoalPopUpVisible = false
        showConfirmationSavedPopUp = false

        val database = Firebase.database
        val userId = auth.currentUser?.uid

        val goalToChangeRef = database.getReference("users").child(userId!!)

        // Create a map with the key-value pair to update
        val updates = hashMapOf<String, Any>(goalPath to textFieldValue.toDouble())

        // Update the value in the database
        goalToChangeRef.updateChildren(updates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showConfirmationSavedPopUp = false
                } else {
                    // Handle the update failure
                    println("Failed to update value: ${task.exception?.message}")
                }
            }
    }

    // Press Cancel (Pop Up)
    val cancelEditAction: () -> Unit = {
        editGoalPopUpVisible = false
    }
    // Press Yes (Pop Up)
    val confirmEditActionYes: () -> Unit = {
        editGoalPopUpVisible = false
        showConfirmationSavedPopUp = true
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

        Text(text = "Exercise Report", fontSize = 30.sp, color = Color(0xFF11435C))

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

        // Exercise title and arrows
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = titleChart,
                fontSize = 20.sp,
                color = Color(0xFF154E1C),
                fontWeight = FontWeight.Bold
            )
        }


        // graph
        Box(
            modifier = Modifier
                .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)
                .fillMaxWidth()
                .height(150.dp)
                .border(2.dp, Color(0xFF1A8B47), shape = RoundedCornerShape(8.dp))
        ) {
            BarChart(
                modifier = Modifier
                    .height(150.dp)
                    .border(2.dp, Color(0xFF1A8B47), shape = RoundedCornerShape(8.dp)),
                barChartData = barChartExercise(
                    selectedExercise,
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

            goal = loadGoalAverage( {editGoalPopUpVisible = true },
                selectedExercise, selectedPeriod, auth)
        }

        val period = if (reportPeriodDaily) "daily" else if (reportPeriodWeekly) "weekly" else "monthly"

        var textFieldValue by remember { mutableStateOf("") }
        val goalExercise = when (selectedExercise) {
            1 -> "run"
            2 -> "walk"
            else -> "cycle"
        }
        val goalPeriod = when (selectedPeriod) {
            1 -> "Daily"
            2 -> "Weekly"
            else -> "Monthly"
        }
        val goalPath = goalExercise + goalPeriod + "Goal"

        if (editGoalPopUpVisible) {

            textFieldValue = ""

            AlertDialog(
                modifier = Modifier.border(2.dp, Color(0xFF1A8B47), shape = RoundedCornerShape(25.dp)),
                containerColor = Color(0xFFA2F0C1),
                onDismissRequest = { editGoalPopUpVisible = false },
                title = {
                    Text(
                        text = "Edit your $period goal",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF154E1C)
                    )
                },
                text = {
                    Column {
                        OutlinedTextField(
                            value = textFieldValue,
                            textStyle = TextStyle(
                                color = Color(0xFF154E1C)
                            ),
                            onValueChange = { textFieldValue = it },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number
                            ),
                            label = { Text("New Goal", color = Color(0xFF154E1C)) }
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = { confirmEditActionYes() }
                    ) {
                        Text("Save",color = Color(0xFF154E1C))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { cancelEditAction() }
                    ) {
                        Text("Cancel",color = Color(0xFF154E1C))
                    }
                }
            )
        }

        if (showConfirmationSavedPopUp) {
            AlertDialog(
                modifier = Modifier.border(2.dp, Color(0xFF1A8B47), shape = RoundedCornerShape(25.dp)),
                onDismissRequest = {
                    // Handle dialog dismiss (e.g., when tapping outside the dialog)
                    showConfirmationSavedPopUp = false
                },
                text = { Text("Your goal was updated successfully!") },
                confirmButton = {
                    TextButton(onClick = { confirmEditAction(goalPath, textFieldValue) }) {
                        Text("OK")
                    }
                }
            )
        }

        val avg = if (getAverage(selectedExercise, auth) < 0 ) 0.0 else getAverage(selectedExercise, auth)

        if (goal != 0.0) {
            if (goal > avg) {

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

        ShowExerciseList(selectedExercise, auth)

    }
}

@Composable
fun loadGoalAverage(
    onDismiss: () -> Unit,
    selectedExercise: Int,                  // 1 - Run, 2 - Walk, 3 - Cycle
    selectedPeriod: Int,                    // 1 - Daily, 2 - Weekly, 3 - Monthly
    auth: FirebaseAuth
): Double {

    val goalExercise = when (selectedExercise) {
        1 -> "run"
        2 -> "walk"
        else -> "cycle"
    }
    val goalPeriod = when (selectedPeriod) {
        1 -> "Daily"
        2 -> "Weekly"
        else -> "Monthly"
    }
    val goalPath = goalExercise + goalPeriod + "Goal"

    val period = when (selectedPeriod) {
        1 -> "Daily"
        2 -> "Weekly"
        else -> "Monthly"
    }

    var goal by remember { mutableDoubleStateOf(0.0) }
    var showDefineGoalPopUpVisible by remember { mutableStateOf(false) }
    var showConfirmationDefinedPopUp by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf("") }

    // Press OK (Pop Up)
    val confirmDefineAction: (String, String) -> Unit = { goalDefinePath, textDefineValue ->
        showDefineGoalPopUpVisible = false
        showConfirmationDefinedPopUp = false

        val database = Firebase.database
        val userId = auth.currentUser?.uid

        val goalToDefineRef = database.getReference("users").child(userId!!)

        // Create a map with the key-value pair to update
        val updates = hashMapOf<String, Any>(goalDefinePath to textDefineValue.toDouble())

        // Update the value in the database
        goalToDefineRef.updateChildren(updates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showConfirmationDefinedPopUp = false
                } else {
                    // Handle the update failure
                    println("Failed to update value: ${task.exception?.message}")
                }
            }
    }

    // Press Cancel (Pop Up)
    val cancelDefineAction: () -> Unit = {
        showDefineGoalPopUpVisible = false
    }
    // Press Yes (Pop Up)
    val confirmDefineActionYes: () -> Unit = {
        showDefineGoalPopUpVisible = false
        showConfirmationDefinedPopUp = true
    }

    DisposableEffect(auth, goalPath) {
        val database = Firebase.database
        val userId = auth.currentUser?.uid

        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    goal = dataSnapshot.getValue(Double::class.java) ?: 0.0
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Error: ${databaseError.message}")
            }
        }

        userId?.let {
            database.getReference("users").child(it).child(goalPath)
                .addValueEventListener(listener)

            onDispose {
                // Remove the listener when the composable is disposed
                database.getReference("users").child(it).child(goalPath)
                    .removeEventListener(listener)
            }
        }!!
    }

    if (showDefineGoalPopUpVisible) {

        textFieldValue = ""

        AlertDialog(
            modifier = Modifier.border(2.dp, Color(0xFF1A8B47), shape = RoundedCornerShape(25.dp)),
            containerColor = Color(0xFFA2F0C1),
            onDismissRequest = { showDefineGoalPopUpVisible = false },
            title = {
                Text(
                    text = "Define your $period goal",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF154E1C)
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = textFieldValue,
                        textStyle = TextStyle(
                            color = Color(0xFF154E1C)
                        ),
                        onValueChange = { textFieldValue = it },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        label = { Text("New Goal", color = Color(0xFF154E1C)) }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { confirmDefineActionYes() }
                ) {
                    Text("Save",color = Color(0xFF154E1C))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { cancelDefineAction() }
                ) {
                    Text("Cancel",color = Color(0xFF154E1C))
                }
            }
        )
    }

    if (showConfirmationDefinedPopUp) {
        AlertDialog(
            modifier = Modifier.border(2.dp, Color(0xFF1A8B47), shape = RoundedCornerShape(25.dp)),
            containerColor = Color(0xFFA2F0C1),
            onDismissRequest = {
                // Handle dialog dismiss (e.g., when tapping outside the dialog)
                showConfirmationDefinedPopUp = false
            },
            text = { Text("Your goal was defined successfully!", color = Color(0xFF0A361C)) },
            confirmButton = {
                TextButton(onClick = { confirmDefineAction(goalPath, textFieldValue) }) {
                    Text("OK", color = Color(0xFF0A361C))
                }
            }
        )
    }

    if (goal != 0.0) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(                                // Show/Set goal
                modifier = Modifier
                    .background(Color(0xFFA2F0C1), shape = RoundedCornerShape(17.dp))
                    .border(2.dp, Color(0xFF1A8B47), shape = RoundedCornerShape(17.dp))
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

                    Spacer(modifier = Modifier.width(30.dp))

                    Text(
                        text = if (isDecimalPartZero(goal)) "${goal.toInt()} km" else "$goal km",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(top = 16.dp),
                        textAlign = TextAlign.End
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.pencil_icon),
                        contentDescription = "Edit Icon",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(27.dp)
                            .padding(top = 15.dp)
                            .clickable {
                                onDismiss()
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            Box(                                // Average
                modifier = Modifier
                    .background(Color(0xFFA2F0C1), shape = RoundedCornerShape(17.dp))
                    .border(2.dp, Color(0xFF1A8B47), shape = RoundedCornerShape(17.dp))
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

                    Spacer(modifier = Modifier.width(20.dp))

                    val avg = if (getAverage(selectedExercise, auth) < 0) 0.0 else getAverage(selectedExercise, auth)

                    val formattedAvg = if (avg > 100) {
                        String.format(Locale.US, "%.0f", avg)
                    } else {
                        String.format(Locale.US, "%.1f", avg)
                    }.toDouble()

                    Text(
                        text = if (isDecimalPartZero(formattedAvg)) "${formattedAvg.toInt()} km" else "$formattedAvg km",
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
                    .background(Color(0xFFA2F0C1), shape = RoundedCornerShape(17.dp))
                    .border(2.dp, Color(0xFF1A8B47), shape = RoundedCornerShape(17.dp)),
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
                        showDefineGoalPopUpVisible = true
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

    return goal
}

@Composable
private fun getDailyGoal(selectedExercise: Int, auth: FirebaseAuth): Double {

    var goal by remember { mutableDoubleStateOf(0.0)}
    DisposableEffect(auth) {
        val database = Firebase.database
        val userId = auth.currentUser?.uid

        val exercise = when (selectedExercise) {
            1 -> "run"
            2 -> "walk"
            else -> "cycle"
        }
        val goalPath = exercise + "DailyGoal"

        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    goal = dataSnapshot.getValue(Double::class.java) ?: 0.0
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Error: ${databaseError.message}")
            }
        }

        userId?.let {
            database.getReference("users").child(it).child(goalPath)
                .addValueEventListener(listener)

            onDispose {
                // Remove the listener when the composable is disposed
                database.getReference("users").child(it).child(goalPath)
                    .removeEventListener(listener)
            }
        }!!
    }
    return goal
}

@Composable
private fun getAverage(selectedExercise: Int, auth: FirebaseAuth): Double {
    val database = com.google.firebase.Firebase.database
    val userId = auth.currentUser?.uid
    var exerciseData: List<Exercise> by remember { mutableStateOf(emptyList()) }

    val category = when (selectedExercise) {
        1 -> "Run"
        2 -> "Walk"
        else -> "Cycle"
    }
    val exercisePath = database.getReference("users/$userId/$category")

    // Use LaunchedEffect to fetch data asynchronously
    LaunchedEffect(Unit) {
        exercisePath.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val dataList = mutableListOf<Exercise>()
                for (i in snapshot.children) {
                    val exercise = i.getValue(Exercise::class.java)
                    exercise?.let {
                        it.type = category
                        dataList.add(it)
                    }
                }
                exerciseData += dataList
            }
        }
    }
    return calculateAverageDistance(exerciseData).toDouble()
}

private fun calculateAverageDistance(exercises: List<Exercise>): Float {
    if (exercises.isEmpty()) {
        return 0.0f // or any default value when the list is empty
    }

    val totalDistance = exercises.sumOf { it.distance.toDouble() }
    return (totalDistance / exercises.size).toFloat()
}


private fun isDecimalPartZero(number: Double): Boolean {
    return number % 1.0 == 0.0
}

@Composable
private fun ShowExerciseList(selectedExercise: Int, auth: FirebaseAuth) {
    val database = com.google.firebase.Firebase.database
    val userId = auth.currentUser?.uid
    var exerciseData: List<Exercise> by remember { mutableStateOf(emptyList()) }

    val category = when (selectedExercise) {
        1 -> "Run"
        2 -> "Walk"
        else -> "Cycle"
    }
    val exercisePath = database.getReference("users/$userId/$category")

    // Use LaunchedEffect to fetch data asynchronously
    LaunchedEffect(Unit) {
        exercisePath.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val dataList = mutableListOf<Exercise>()
                for (i in snapshot.children) {
                    val exercise = i.getValue(Exercise::class.java)
                    exercise?.let {
                        it.type = category
                        dataList.add(it)
                    }
                }
                exerciseData += dataList
            }
        }
    }

    // Display the data in a Column with dynamic Rows
    LazyColumn {
        items(exerciseData.reversed()) { exercise ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(6.dp)
                    .background(
                        Color(0xFFA2F0C1),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(2.dp, Color(0xFF1A8B47), shape = RoundedCornerShape(12.dp))
                    .clickable { },
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = exercise.type,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        )
                        if (exercise.type == "Walk") {
                            Image(
                                painter = painterResource(id = R.drawable.walk_icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp)
                            )
                        } else if (exercise.type == "Run") {
                            Image(
                                painter = painterResource(id = R.drawable.run_icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp)
                            )
                        } else {
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
                        text = convertDate(exercise.date),
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 10.sp
                        )
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${String.format("%.2f", exercise.distance)}km",
                        style = TextStyle(
                            fontSize = 15.sp
                        )
                    )
                    Text(
                        text = "Distance",
                        style = TextStyle(
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = formatAveragePace(exercise.averagePace),
                        style = TextStyle(
                            fontSize = 15.sp
                        )
                    )
                    Text(
                        text = "Rhythm",
                        style = TextStyle(
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = convertMillisecond(exercise.time),
                        style = TextStyle(
                            fontSize = 15.sp
                        )
                    )
                    Text(
                        text = "Time",
                        style = TextStyle(
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val goal = getDailyGoal(selectedExercise, auth)

                    val goalAchieved = exercise.distance >= goal

                    if (goalAchieved) {
                        Icon(
                            painter = painterResource(id = R.drawable.green_check_icon),
                            contentDescription = "goalAchievedCheck",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(20.dp)
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.red_x_icon),
                            contentDescription = "goalAchievedCheck",
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
fun barChartExercise(
    selectedExercise: Int,
    selectedPeriod: Int,
    auth: FirebaseAuth
):BarChartData {
    val database = com.google.firebase.Firebase.database
    val userId = auth.currentUser?.uid
    var exerciseData: List<Exercise> by remember { mutableStateOf(emptyList()) }

    val barChartListSize = if (selectedPeriod == 1) 7 else if (selectedPeriod == 2) 10 else 12
    var yStepSize = 4

    val category = when (selectedExercise) {
        1 -> "Run"
        2 -> "Walk"
        else -> "Cycle"
    }
    val exercisePath = database.getReference("users/$userId/$category")

    // Use LaunchedEffect to fetch data asynchronously
    LaunchedEffect(Unit) {
        exercisePath.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val dataList = mutableListOf<Exercise>()
                for (i in snapshot.children) {
                    val exercise = i.getValue(Exercise::class.java)
                    exercise?.let {
                        it.type = category
                        dataList.add(it)
                    }
                }
                exerciseData += dataList
            }
        }
    }

    var maxRange = 0
    if (selectedPeriod == 1) {
        val splitByDay = splitExercisesByDay(exerciseData)
        val size = splitByDay.size
        var control = 0
        for (i in 0 until barChartListSize) {
            if (control < size) {
                if (maxRange < sumDistances(splitByDay[i])) {
                    Log.e("Max Range Daily", sumDistances(splitByDay[i]).toString())
                    maxRange = sumDistances(splitByDay[i]).toInt()
                }
                control++
            }
        }
    } else if (selectedPeriod == 2) {
        val splitByWeek = splitExercisesByWeek(exerciseData)
        val size = splitByWeek.size
        var control = 0
        for (i in 0 until barChartListSize) {
            if (control < size) {
                if (maxRange < sumDistances(splitByWeek[i])) {
                    Log.e("Max Range Weekly", sumDistances(splitByWeek[i]).toString())
                    maxRange = sumDistances(splitByWeek[i]).toInt()
                }
                control++
            }
        }
    } else {
        for (i in 0 until barChartListSize) {
            if (maxRange < sumDistancesMonth(exerciseData, i)) {
                Log.e("Max Range Monthly", sumDistancesMonth(exerciseData, i).toString())
                maxRange = sumDistancesMonth(exerciseData, i).toInt()
            }
        }
    }
    maxRange = (maxRange*1.2).toInt()

    if (maxRange == 0) {
        maxRange = 1
    }

    val barData = getBarChartDataUpdated(
        exerciseData,
        barChartListSize,
        DataCategoryOptions(),
        selectedPeriod
    )

    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .steps(barData.size - 1)
        .backgroundColor(Color(0xFFA2F0C1))
        .bottomPadding(20.dp)
        .labelAndAxisLinePadding(8.dp)
        .axisLabelAngle(if (selectedPeriod == 1) 0f else 30f)
        .axisLabelFontSize(12.sp)
        .startDrawPadding(20.dp)
        .labelData { index -> barData[index].label }
        .build()

    val yAxisData = AxisData.Builder()
        .steps(yStepSize)
        .backgroundColor(Color(0xFFA2F0C1))
        .labelAndAxisLinePadding(10.dp)
        .axisOffset(10.dp)
        .labelData { index ->
            while (yStepSize > maxRange) {
                yStepSize--
            }
            if(yStepSize == 0){
                yStepSize = 1
            }
            (index * (maxRange / yStepSize)).toString() + " km" }
        .build()

    return BarChartData(
        chartData = barData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        backgroundColor = Color(0xFFA2F0C1),
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
    exercises: List<Exercise>,
    listSize: Int,
    dataCategoryOptions: DataCategoryOptions,
    selectedPeriod: Int
): List<BarData> {
    val list = arrayListOf<BarData>()
    val monthlyList = arrayListOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    if (selectedPeriod == 1) {                      // Run Daily
        val splitByDay = splitExercisesByDay(exercises)
        val size = splitByDay.size
        var control = 0

        if (splitByDay.isNotEmpty()) {
            var dayOfWeek = LocalDate.ofInstant(Instant.ofEpochMilli(splitByDay[0][0].date), ZoneId.systemDefault()).dayOfWeek

            for (i in 0 until listSize) {
                if (control < size) {
                    val distanceString = "%.2f".format(sumDistances(splitByDay[i])).replace(",", ".")
                    val point = Point(i.toFloat(), distanceString.toFloat())
                    list.add(
                        BarData(
                            point = point,
                            color = if (control == size-1 ) Color(0xFF154E1C) else Color(0xFF1A8B47),
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
                            color = Color(0xFF1A8B47),
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
    } else if (selectedPeriod == 2) {              // Run Weekly
        val splitByWeek = splitExercisesByWeek(exercises)
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
                    val distanceString = "%.2f".format(sumDistances(splitByWeek[i])).replace(",", ".")
                    val point = Point(i.toFloat(), distanceString.toFloat())

                    list.add(
                        BarData(
                            point = point,
                            color = if (control == size-1 ) Color(0xFF154E1C) else Color(0xFF1A8B47),
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
                            color = Color(0xFF1A8B47),
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
    } else {                                      // Run Monthly
        if (exercises.isNotEmpty()) {
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
                val distanceString = "%.2f".format(sumDistancesMonth(exercises, index)).replace(",", ".")
                val point = Point(index.toFloat(), distanceString.toFloat())

                list.add(
                    BarData(
                        point = point,
                        color = if (index == control) Color(0xFF154E1C) else Color(0xFF1A8B47),
                        dataCategoryOptions = dataCategoryOptions,
                        label = monthlyList[index]
                    )
                )
            }
        }
        else {
            for (index in 0 until listSize) {
                val point = Point(index.toFloat(), 0.toFloat())
                list.add(
                    BarData(
                        point = point,
                        color = Color(0xFF1A8B47),
                        dataCategoryOptions = dataCategoryOptions,
                        label = monthlyList[index]
                    )
                )
            }
        }
    }
    return list
}
@RequiresApi(34)
private fun splitExercisesByWeek(exercises: List<Exercise>): List<ArrayList<Exercise>> {
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
private fun sumDistancesMonth(exercises: List<Exercise>, monthIndex: Int): Double {
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

    return monthExercises.sumOf { it.distance.toDouble() }
}

private fun sumDistances(exercises: ArrayList<Exercise>) : Double {
    return exercises.sumOf { it.distance.toDouble() }
}

@RequiresApi(34)
private fun splitExercisesByDay(exercises: List<Exercise>): List<ArrayList<Exercise>> {
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

private fun convertDate(date: Long): String {
    val instant = Instant.ofEpochMilli(date)
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return localDateTime.format(formatter)
}

private fun convertMillisecond(millisecond: Long): String {
    val totalSeconds = millisecond / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "${minutes}m${seconds}s"
}

private fun formatAveragePace(averagePace: Float): String {
    return String.format(Locale.getDefault(), "%.2f min/km", averagePace)
}