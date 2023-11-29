package com.example.dailysync.navigation

sealed class Screens(val route: String) {
    object Home : Screens("home_screen/{result}")
    object Exercise : Screens("exercise/{result}")
    object Read : Screens("read_screen")
    object Sleep : Screens("sleep_screen/{result}")
}