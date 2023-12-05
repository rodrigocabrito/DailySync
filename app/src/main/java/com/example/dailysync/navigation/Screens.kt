package com.example.dailysync.navigation

sealed class Screens(val route: String) {

    // Start
    object Login : Screens("login_screen")
    object SignUp : Screens("signup_screen")

    // Footer
    object Home : Screens("home_screen")
    object Reports : Screens("reports_screen")
    object Community : Screens("community_screen")
    object Profile : Screens("profile_screen")

    // Exercise
    object StartExercise : Screens("start_exercise_screen/{category}")
    object DuringExercise : Screens("during_exercise_screen/{category}")
    object SaveExercise : Screens("save_exercise_screen/{category}")

    // Sleep
    object RegisterSleep : Screens("register_sleep_screen")
    object DefineSleepSchedule : Screens("define_sleep_schedule_screen/{bedTime}/{awakeTime}/{target}")
    object EditSleepSchedule : Screens("edit_sleep_schedule_screen/{target}")

    // Read
    // TODO ALL READ ROUTES
}