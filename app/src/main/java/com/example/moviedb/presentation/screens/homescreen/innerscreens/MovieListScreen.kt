package com.example.moviedb.presentation.screens.homescreen.innerscreens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.moviedb.presentation.screens.homescreen.HomeScreenState
import com.example.moviedb.presentation.screens.homescreen.HomeScreenUiEvent




import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moviedb.presentation.component.MovieItem


@Composable
fun MovieListScreen(
    navController: NavController,
    homeScreenState: HomeScreenState,
    onEvent: (HomeScreenUiEvent) -> Unit
) {
    if (homeScreenState.movieList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
        ) {
            items(homeScreenState.movieList.size) { index ->
                MovieItem(
                    movie = homeScreenState.movieList[index],
                    navController = navController
                )
                Spacer(modifier = Modifier.height(16.dp))

                Log.d("TestViewModel-TestViewModel", "movieScreen: $homeScreenState")
                Log.d("TestViewModel-TestViewModel", "can paginate: ${index >= homeScreenState.movieList.size - 1 && !homeScreenState.isLoading}")
                if (index >= homeScreenState.movieList.size - 1 && !homeScreenState.isLoading) {
                    onEvent(HomeScreenUiEvent.Paginate(homeScreenState.category))
                }
            }
        }
    }
}


