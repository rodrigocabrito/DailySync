package com.example.dailysync.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dailysync.BookViewModel
import com.example.dailysync.ExerciseViewModel
import com.example.dailysync.bookModels.Items
import com.example.dailysync.community.Community
import com.example.dailysync.community.SelectFromList
import com.example.dailysync.community.SelectToShare
import com.example.dailysync.home.Home
import com.example.dailysync.home.exercise.DuringExercise
import com.example.dailysync.home.exercise.SaveExercise
import com.example.dailysync.home.exercise.StartExercise
import com.example.dailysync.home.read.BookDetailsScreen
import com.example.dailysync.home.read.MyLibrary
import com.example.dailysync.home.read.ReadingSession
import com.example.dailysync.home.read.SearchScreen
import com.example.dailysync.home.sleep.DefineSleepSchedule
import com.example.dailysync.home.sleep.EditSleepSchedule
import com.example.dailysync.home.sleep.RegisterSleep
import com.example.dailysync.login.Login
import com.example.dailysync.login.SignUp
import com.example.dailysync.notifications.Notifications
import com.example.dailysync.profile.Profile
import com.example.dailysync.report.ExerciseReport
import com.example.dailysync.report.ReadReport
import com.example.dailysync.report.Reports
import com.example.dailysync.report.SleepReport
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun NavGraph (navController: NavHostController, auth: FirebaseAuth, bookViewModel: BookViewModel, exerciseViewModel: ExerciseViewModel){
    NavHost(
        navController = navController,
        startDestination = Screens.Login.route)
    {
        // Start
        composable(route = Screens.Login.route){
            Login(navController = navController, auth = auth)
        }
        composable(route = Screens.SignUp.route){
            SignUp(navController = navController, auth = auth)
        }

        // ##################################################################################################################################################################################

        // Footer
        composable(route = Screens.Home.route){
            Home(navController = navController, auth = auth)
        }
        composable(route = Screens.Reports.route + "?selectedExercise={selectedExercise}&selectedPeriod={selectedPeriod}"){ navBackStack ->
            val selectedExercise: Int = navBackStack.arguments?.getString("selectedExercise")?.toIntOrNull()?:1
            val selectedPeriod: Int = navBackStack.arguments?.getString("selectedPeriod")?.toIntOrNull()?:1
            if (Build.VERSION.SDK_INT >= 34) {
                Reports(navController = navController, selectedExercise, selectedPeriod, auth = auth)
            }
        }
        composable(route = Screens.Community.route){
            Community(navController = navController)
        }
        composable(route = Screens.Profile.route){
            Profile(navController = navController, auth = auth)
        }

        // ##################################################################################################################################################################################

        // Exercise
        composable(route = Screens.StartExercise.route + "?category={category}"){ navBackStack ->
            // extracting the argument
            val categoryShow: Int = navBackStack.arguments?.getString("category")?.toIntOrNull()?:1
            StartExercise(navController = navController, categoryShow = categoryShow)
        }
        composable(route = Screens.DuringExercise.route + "?category={category}"){ navBackStack ->
            // extracting the argument
            val categoryShow: Int = navBackStack.arguments?.getString("category")?.toIntOrNull()?:1
            DuringExercise(navController = navController, categoryShow = categoryShow, viewModel = exerciseViewModel)
        }
        composable(route = Screens.SaveExercise.route + "?category={category}"){ navBackStack ->
            // extracting the argument
            val categoryShow: Int = navBackStack.arguments?.getString("category")?.toIntOrNull()?:1
            val timeShow: Long = navBackStack.arguments?.getString("time")?.toLongOrNull()?:1L
            val averagePaceShow: Float = navBackStack.arguments?.getString("averagePace")?.toFloatOrNull()?:1.0f
            val distanceShow: Float = navBackStack.arguments?.getString("distance")?.toFloatOrNull()?:1.0f
            SaveExercise(navController = navController, categoryShow = categoryShow, auth = auth, timeShow = timeShow, averagePaceShow = averagePaceShow, distanceShow = distanceShow, viewModel = exerciseViewModel)
        }

        // ##################################################################################################################################################################################

        // Sleep
        composable(route = Screens.RegisterSleep.route){
            RegisterSleep(navController = navController, auth = auth)
        }
        composable(route = Screens.DefineSleepSchedule.route + "?bedTime={bedTime}&awakeTime={awakeTime}&target={target}") { navBackStack ->
            // extracting the arguments
            val bedTime: String? = navBackStack.arguments?.getString("bedTime")
            val awakeTime: String? = navBackStack.arguments?.getString("awakeTime")
            val targetShow: Int = navBackStack.arguments?.getString("target")?.toIntOrNull() ?: 16

            val bedTimeShow: String = bedTime?.let {
                val parts = it.split(":")
                if (parts.size == 2) {
                    val hours = parts[0].toIntOrNull() ?: 23
                    val minutes = parts[1].toIntOrNull() ?: 0
                    "$hours:${String.format("%02d", minutes)}"
                } else {
                    "23:00" // Default value if the format is not as expected
                }
            } ?: "23:00" // Default value if bedTime is null

            val awakeTimeShow: String = awakeTime?.let {
                val parts = it.split(":")
                if (parts.size == 2) {
                    val hours = parts[0].toIntOrNull() ?: 7
                    val minutes = parts[1].toIntOrNull() ?: 0
                    "$hours:${String.format("%02d", minutes)}"
                } else {
                    "7:00" // Default value if the format is not as expected
                }
            } ?: "7:00" // Default value if awakeTime is null

            DefineSleepSchedule(navController = navController, bedTimeShow = bedTimeShow, awakeTimeShow = awakeTimeShow, targetShow = targetShow)
        }
        composable(route = Screens.EditSleepSchedule.route + "?target={target}"){navBackStack ->
            // extracting the argument
            val targetString: String? = navBackStack.arguments?.getString("target")
            val regexResult = Regex("""(\d+)h(\d+)""").find(targetString ?: "")
            val (hours, minutes) = if (regexResult != null) {
                val (hoursGroup, minutesGroup) = regexResult.destructured
                Pair(hoursGroup.toInt(), minutesGroup.toInt())
            } else {
                Pair(0, 0) // Default values if the format is not as expected
            }
            val targetShow: String = "$hours" + "h" + "$minutes"
            EditSleepSchedule(navController = navController, auth = auth, targetShow = targetShow)
        }

        // ##################################################################################################################################################################################

        // Read
        composable(Screens.Search.route) {
            SearchScreen(
                navController = navController,
                bookViewModel = bookViewModel
            )
        }
        composable(route = Screens.BookDetails.route) {
            val item = navController.previousBackStackEntry?.savedStateHandle?.get<Items>("item")
            if (item != null) {
                BookDetailsScreen(
                    navController = navController,
                    bookViewModel = bookViewModel,
                    item = item
                )
            }
        }
        composable(Screens.MyLibrary.route){
            MyLibrary(navController = navController, bookViewModel = bookViewModel)
        }
        composable(Screens.ReadingSession.route){
            val item = navController.previousBackStackEntry?.savedStateHandle?.get<Items>("item")
            if (item != null) {
                ReadingSession(navController = navController, bookViewModel = bookViewModel, item = item)
            }
        }

        // ##################################################################################################################################################################################

        // Notifications
        composable(Screens.Notifications.route){
            Notifications(navController = navController, auth = auth)
        }

        // ##################################################################################################################################################################################

        // Report
        composable(Screens.ExerciseReport.route + "?selectedExercise={selectedExercise}&selectedPeriod={selectedPeriod}"){navBackStack ->
            val selectedExercise: Int = navBackStack.arguments?.getString("selectedExercise")?.toIntOrNull() ?: 1
            val selectedPeriod: Int = navBackStack.arguments?.getString("selectedPeriod")?.toIntOrNull() ?: 1
            if (Build.VERSION.SDK_INT >= 34) {
                ExerciseReport(navController = navController, selectedExercise, selectedPeriod, auth = auth)
            }
        }
        composable(Screens.SleepReport.route + "?selectedPeriod={selectedPeriod}"){navBackStack ->
            val selectedPeriod: Int = navBackStack.arguments?.getString("selectedPeriod")?.toIntOrNull() ?: 1
            if (Build.VERSION.SDK_INT >= 34) {
                SleepReport(navController = navController, selectedPeriod, auth = auth)
            }
        }
        composable(Screens.ReadReport.route + "?selectedPeriod={selectedPeriod}"){navBackStack ->
            val selectedPeriod: Int = navBackStack.arguments?.getString("selectedPeriod")?.toIntOrNull() ?: 1
            if (Build.VERSION.SDK_INT >= 34) {
                ReadReport(navController = navController, selectedPeriod, auth = auth)
            }
        }

        // ##################################################################################################################################################################################
        // Community
        composable(route = Screens.SelectToShare.route){
            SelectToShare(navController = navController)
        }
        composable(route = Screens.SelectFromList.route + "?type={type}"){ navBackStack ->
            val type: String? = navBackStack.arguments?.getString("type")

            if (type == "Exercise")
                SelectFromList(navController = navController, auth = auth, type = "Exercise")
            else if(type == "Sleep")
                SelectFromList(navController = navController, auth = auth, type = "Sleep")
            else if(type == "Read")
                SelectFromList(navController = navController, auth = auth, type = "Read")
        }

    }
}