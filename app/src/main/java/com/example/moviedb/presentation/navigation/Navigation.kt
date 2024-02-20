package com.example.moviedb.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moviedb.presentation.screens.homescreen.HomeScreen
import com.example.moviedb.presentation.screens.moviedetail.MoviesDetailScreen
import com.example.moviedb.presentation.screens.search.SearchScreen
import com.example.moviedb.presentation.screens.splash.SplashScreen
import com.example.moviedb.presentation.screens.tvshowdetail.TvShowDetailScreen

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

        composable(
            route = Screens.TvShowDetail.route + "/{show_id}",
            arguments = listOf(
                navArgument("show_id") {
                    type = NavType.IntType
                    defaultValue = -1
                    nullable = false
                }
            )
        ) {
            TvShowDetailScreen(navController)
        }

        composable(
            route = Screens.MovieDetail.route + "/{movie_id}",
            arguments = listOf(
                navArgument("movie_id") {
                    type = NavType.IntType
                    defaultValue = -1
                    nullable = false
                }
            )
        ) {
            MoviesDetailScreen(navController)
        }

        composable(Screens.Search.route) {
            SearchScreen(navController = navController)
        }
    }
}