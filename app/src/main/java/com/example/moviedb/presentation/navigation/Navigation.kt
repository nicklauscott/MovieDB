package com.example.moviedb.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moviedb.presentation.screens.homescreen.HomeScreen
import com.example.moviedb.presentation.screens.splash.SplashScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.SPlash.route) {
        composable(Screens.SPlash.route) {
            SplashScreen(navController = navController)
        }

        composable(Screens.Home.route) {
            HomeScreen(navController = navController)
        }
    }
}