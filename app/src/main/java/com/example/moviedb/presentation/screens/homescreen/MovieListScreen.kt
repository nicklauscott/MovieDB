package com.example.moviedb.presentation.screens.homescreen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moviedb.presentation.navigation.Screens
import com.example.moviedb.presentation.screens.homescreen.component.AppTopBar
import com.example.moviedb.presentation.screens.homescreen.component.BottomNavigationBar
import com.example.moviedb.presentation.screens.homescreen.innerscreens.MovieListScreen
import com.example.moviedb.presentation.screens.homescreen.innerscreens.TvShowListScreen
import com.example.moviedb.util.Category

@Composable
fun HomeScreen(navController: NavController) {

    val homeScreenViewModel = hiltViewModel<HomeScreenViewModel>()
    val homeScreenState = homeScreenViewModel.homeScreenState.collectAsState().value
    val bottomNavController = rememberNavController()

    val currentCategory = remember {
        mutableStateOf(homeScreenState.category)
    }


    Scaffold(bottomBar = {
        BottomNavigationBar(
            bottomNavController = bottomNavController,
            homeScreenState.isMovieListScreen,
            onEvent = {
                homeScreenViewModel.onEvent(HomeScreenUiEvent.Navigate)
                currentCategory.value = Category.Popular
            }
        )
    }, topBar = {
        AppTopBar(homeScreenState = homeScreenState,
            currentCategory = currentCategory,
            onChangeCategory = {
                currentCategory.value = it
                homeScreenViewModel.onEvent(HomeScreenUiEvent.SwitchCategory(it))
            }) {
            navController.navigate(Screens.Search.route)
        }
    }) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            NavHost(
                navController = bottomNavController,
                startDestination = Screens.MovieList.route
            ) {
                composable(Screens.MovieList.route) {
                    MovieListScreen(
                        navController = navController,
                        homeScreenState = homeScreenState,
                        onEvent = homeScreenViewModel::onEvent
                    )
                }
                composable(Screens.TvShowLIst.route) {
                    TvShowListScreen(
                        navController = navController,
                        homeScreenState = homeScreenState,
                        onEvent = homeScreenViewModel::onEvent
                    )
                }
            }
        }
    }
}