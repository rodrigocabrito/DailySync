package com.example.dailysync.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dailysync.Read.Read
import com.example.dailysync.Exercise.Exercise
import com.example.dailysync.Sleep.Sleep
import com.example.dailysync.Home

@Composable
fun NavGraph (navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Screens.Home.route)
    {
        composable(route = Screens.Home.route+ "?result={result}"){ navBackStack ->
            var resultShow: Int = navBackStack.arguments?.getString("result")?.toIntOrNull()?:1
            Home(navController = navController, resultShow = resultShow)
        }
        composable(route = Screens.Exercise.route+ "?result={result}"){ navBackStack ->
            //extracting the argument
            var resultShow: Int = navBackStack.arguments?.getString("result")?.toIntOrNull()?:1
            Exercise(navController = navController, resultShow = resultShow)
        }

        composable(route = Screens.Read.route){
            Read(navController = navController)
        }

        composable(route = Screens.Sleep.route+ "?result={result}"){ navBackStack ->
            //extracting the argument
            var resultShow: Int = navBackStack.arguments?.getString("result")?.toIntOrNull()?:1
            Sleep(navController = navController, resultShow = resultShow)
        }
    }
}