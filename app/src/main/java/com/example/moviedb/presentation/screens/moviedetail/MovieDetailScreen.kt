package com.example.moviedb.presentation.screens.moviedetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.moviedb.presentation.component.DetailScreenTopBar
import com.example.moviedb.presentation.component.MovieItem
import com.example.moviedb.presentation.navigation.Screens
import com.example.moviedb.presentation.screens.moviedetail.component.MovieDetailBody
import com.example.moviedb.presentation.screens.moviedetail.component.MovieDetailHeader

@Composable
fun MoviesDetailScreen(navController: NavController, viewModel: MovieDetailViewModel = hiltViewModel()) {

    val state = viewModel.movieDetailState.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            DetailScreenTopBar(onClickBack = { navController.popBackStack() }) {
                navController.navigate(Screens.Search.route)
            }
        }
    ) {
        when {
            state.value.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            else -> {
                when {
                    state.value.movie == null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Error getting movie",
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                fontSize = 15.sp,
                                style = MaterialTheme.typography.bodyMedium,
                                fontFamily = FontFamily.Serif
                            )
                        }
                    }

                    else -> {
                        Column(
                            modifier = Modifier
                                .padding(it)
                                .verticalScroll(scrollState)
                        ) {
                            state.value.movie?.let { movie ->

                                MovieDetailHeader(movie)

                                MovieDetailBody(movie) {
                                    viewModel.onEvent(MovieDetailUiEvent.AddOrRemoveFromMyList)
                                }

                                when {
                                    state.value.isSimilarMoviesLoading -> {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    }

                                    else -> {
                                        if (state.value.similarMovies.isEmpty()) {
                                            Box(
                                                modifier = Modifier
                                                    .height(200.dp)
                                                    .fillMaxWidth(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "Oops! No similar movies available",
                                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                                    fontSize = 15.sp,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontFamily = FontFamily.Serif
                                                )
                                            }
                                        } else {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .wrapContentHeight()
                                            ) {
                                                Column {
                                                    state.value.similarMovies.take(state.value.similarMovies.size / 2)
                                                        .forEach { movie ->
                                                            MovieItem(
                                                                movie = movie,
                                                                navController = navController
                                                            )
                                                        }
                                                }

                                                Column {
                                                    state.value.similarMovies.takeLast(state.value.similarMovies.size / 2)
                                                        .forEach { movie ->
                                                            MovieItem(
                                                                movie = movie,
                                                                navController = navController
                                                            )
                                                        }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}