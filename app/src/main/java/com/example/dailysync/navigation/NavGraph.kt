package com.example.dailysync.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dailysync.community.Community
import com.example.dailysync.home.Home
import com.example.dailysync.home.exercise.DuringExercise
import com.example.dailysync.home.exercise.SaveExercise
import com.example.dailysync.home.exercise.StartExercise
import com.example.dailysync.home.sleep.DefineSleepSchedule
import com.example.dailysync.home.sleep.EditSleepSchedule
import com.example.dailysync.home.sleep.RegisterSleep
import com.example.dailysync.login.Login
import com.example.dailysync.login.SignUp
import com.example.dailysync.profile.EditProfile
import com.example.dailysync.profile.Profile
import com.example.dailysync.report.Reports
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavGraph (navController: NavHostController, auth: FirebaseAuth){
    NavHost(
        navController = navController,
        startDestination = Screens.SignUp.route)
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
        composable(route = Screens.Reports.route){
            Reports(navController = navController, auth = auth)
        }
        composable(route = Screens.Community.route){
            Community(navController = navController, auth = auth)
        }
        composable(route = Screens.Profile.route){
            Profile(navController = navController, auth = auth)
        }

        // ##################################################################################################################################################################################

        // Exercise
        composable(route = Screens.StartExercise.route + "?category={category}"){ navBackStack ->
            // extracting the argument
            val categoryShow: Int = navBackStack.arguments?.getString("category")?.toIntOrNull()?:1
            StartExercise(navController = navController, categoryShow = categoryShow, auth = auth)
        }
        composable(route = Screens.DuringExercise.route + "?category={category}"){ navBackStack ->
            // extracting the argument
            val categoryShow: Int = navBackStack.arguments?.getString("category")?.toIntOrNull()?:1
            DuringExercise(navController = navController, categoryShow = categoryShow, auth = auth)
        }
        composable(route = Screens.SaveExercise.route + "?category={category}"){ navBackStack ->
            // extracting the argument
            val categoryShow: Int = navBackStack.arguments?.getString("category")?.toIntOrNull()?:1
            val timeShow: Long = navBackStack.arguments?.getString("time")?.toLongOrNull()?:1L
            val averagePaceShow: Float = navBackStack.arguments?.getString("averagePace")?.toFloatOrNull()?:1.0f
            val distanceShow: Float = navBackStack.arguments?.getString("distance")?.toFloatOrNull()?:1.0f
            SaveExercise(navController = navController, categoryShow = categoryShow, auth = auth, timeShow = timeShow, averagePaceShow = averagePaceShow, distanceShow = distanceShow)
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
            val targetShow: Int = navBackStack.arguments?.getString("target")?.toIntOrNull() ?: 0

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

            DefineSleepSchedule(navController = navController, auth = auth, bedTimeShow = bedTimeShow, awakeTimeShow = awakeTimeShow, targetShow = targetShow)
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
        // TODO ALL READ PAGES

        // ##################################################################################################################################################################################

        // Profile
        composable(route = Screens.EditProfile.route){
            EditProfile(navController = navController, auth = auth)
        }
    }
}