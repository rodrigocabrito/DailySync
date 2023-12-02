package com.example.dailysync.navigation

sealed class Screens(val route: String) {
    object Login : Screens("login_screen")
    object SignUp : Screens("signup_screen")
    object Home : Screens("home_screen")

    object Exercise : Screens("exercise/{result}")
    object Read : Screens("read_screen")
    object Sleep : Screens("sleep_screen/{result}")
}