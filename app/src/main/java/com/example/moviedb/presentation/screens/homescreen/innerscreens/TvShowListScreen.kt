package com.example.moviedb.presentation.screens.homescreen.innerscreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.moviedb.presentation.component.MovieItem
import com.example.moviedb.presentation.component.TvShowItem
import com.example.moviedb.presentation.screens.homescreen.HomeScreenState
import com.example.moviedb.presentation.screens.homescreen.HomeScreenUiEvent
import com.example.moviedb.util.Category

@Composable
fun TvShowListScreen(
    navController: NavController,
    homeScreenState: HomeScreenState,
    onEvent: (HomeScreenUiEvent) -> Unit
) {

    if (homeScreenState.tvShowList.isEmpty()) {
        if (homeScreenState.category == Category.MyList) {
            if (homeScreenState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Your list is empty",
                        fontSize = 23.sp, style = MaterialTheme.typography.headlineMedium)

                }
            }
        }else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
        ) {
            items(homeScreenState.tvShowList.size) { index ->
                TvShowItem(
                    tvShow = homeScreenState.tvShowList[index],
                    navController = navController
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (index >= homeScreenState.tvShowList.size - 1 && !homeScreenState.isLoading) {
                    onEvent(HomeScreenUiEvent.Paginate(homeScreenState.category))
                }
            }
        }
    }
}