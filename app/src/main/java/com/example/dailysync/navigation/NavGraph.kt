package com.example.dailysync.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dailysync.community.Community
import com.example.dailysync.home.read.Read
import com.example.dailysync.home.exercise.Exercise
import com.example.dailysync.home.sleep.Sleep
import com.example.dailysync.home.Home
import com.example.dailysync.home.exercise.DuringExercise
import com.example.dailysync.home.exercise.SaveExercise
import com.example.dailysync.home.exercise.StartExercise
import com.example.dailysync.home.sleep.DefineSleepSchedule
import com.example.dailysync.home.sleep.EditSleepSchedule
import com.example.dailysync.home.sleep.RegisterSleep
import com.example.dailysync.login.Login
import com.example.dailysync.login.SignUp
import com.example.dailysync.profile.Profile
import com.example.dailysync.report.Reports
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavGraph (navController: NavHostController, auth: FirebaseAuth){
    NavHost(
        navController = navController,
        startDestination = Screens.Home.route)
    {
        // Start
        composable(route = Screens.Login.route){
            Login(navController = navController, auth = auth)
        }
        composable(route = Screens.SignUp.route){
            SignUp(navController = navController, auth = auth)
        }

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

        // Exercise
        composable(route = Screens.StartExercise.route + "?category={category}"){ navBackStack ->
            //extracting the argument
            var categoryShow: Int = navBackStack.arguments?.getString("category")?.toIntOrNull()?:1
            StartExercise(navController = navController, categoryShow = categoryShow, auth = auth)
        }
        composable(route = Screens.DuringExercise.route + "?category={category}"){ navBackStack ->
            //extracting the argument
            var categoryShow: Int = navBackStack.arguments?.getString("category")?.toIntOrNull()?:1
            DuringExercise(navController = navController, categoryShow = categoryShow, auth = auth)
        }
        composable(route = Screens.SaveExercise.route + "?category={category}"){ navBackStack ->
            //extracting the argument
            var categoryShow: Int = navBackStack.arguments?.getString("category")?.toIntOrNull()?:1
            SaveExercise(navController = navController, categoryShow = categoryShow, auth = auth)
        }

        // Sleep
        composable(route = Screens.RegisterSleep.route){
            RegisterSleep(navController = navController, auth = auth)
        }
        composable(route = Screens.DefineSleepSchedule.route + "?bedTime={bedTime}&awakeTime={awakeTime}") { navBackStack ->
            // Extracting the arguments
            val bedTime: String? = navBackStack.arguments?.getString("bedTime")
            val awakeTime: String? = navBackStack.arguments?.getString("awakeTime")

            val bedTimeShow: String = (bedTime?.toIntOrNull() ?: 1).toString()
            val awakeTimeShow: String = (awakeTime?.toIntOrNull() ?: 1).toString()

            DefineSleepSchedule(navController = navController, auth = auth, bedTimeShow = bedTimeShow, awakeTimeShow = awakeTimeShow)
        }

        composable(route = Screens.EditSleepSchedule.route + "?target={target}"){navBackStack ->
            var targetShow: Double = (navBackStack.arguments?.getString("target")?.toIntOrNull()?:1).toDouble()
            EditSleepSchedule(navController = navController, auth = auth, targetShow = targetShow)
        }






        composable(route = Screens.Exercise.route + "?result={result}"){ navBackStack ->
            //extracting the argument
            var resultShow: Int = navBackStack.arguments?.getString("result")?.toIntOrNull()?:1
            Exercise(navController = navController, resultShow = resultShow)
        }

        composable(route = Screens.Read.route){
            Read(navController = navController)
        }

        composable(route = Screens.Sleep.route + "?result={result}"){ navBackStack ->
            //extracting the argument
            var resultShow: Int = navBackStack.arguments?.getString("result")?.toIntOrNull()?:1
            Sleep(navController = navController, resultShow = resultShow)
        }
    }
}