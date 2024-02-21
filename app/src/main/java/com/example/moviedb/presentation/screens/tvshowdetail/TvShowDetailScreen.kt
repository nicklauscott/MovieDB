package com.example.moviedb.presentation.screens.tvshowdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.moviedb.presentation.component.DetailScreenTopBar
import com.example.moviedb.presentation.component.TvShowItem
import com.example.moviedb.presentation.navigation.Screens
import com.example.moviedb.presentation.screens.tvshowdetail.component.EpisodeCell
import com.example.moviedb.presentation.screens.tvshowdetail.component.SeasonToggle
import com.example.moviedb.presentation.screens.tvshowdetail.component.TvShowDetailBody
import com.example.moviedb.presentation.screens.tvshowdetail.component.TvShowDetailHeader

@Composable
fun TvShowDetailScreen(
    navController: NavController,
    viewModel: TvShowDetailViewModel = hiltViewModel()
) {

    val state = viewModel.tvShowDetailScreenState.collectAsState()
    val scrollState = rememberScrollState()


    val isEpisodeVisible = remember {
        mutableStateOf(true)
    }

    val currentSeason = remember {
        mutableIntStateOf(1)
    }

    Scaffold(
        topBar = {
            DetailScreenTopBar(onClickBack = { navController.popBackStack() }) {
                navController.navigate(Screens.Search.route)
            }
        }
    ) {
        when (state.value.tvShow) {
            null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .verticalScroll(scrollState)
                ) {

                    state.value.tvShow?.let { tvShow ->

                        // show detail
                        TvShowDetailHeader(tvShow)

                        // show body
                        TvShowDetailBody(
                            tvShow,
                            isEpisodeVisible.value,
                            onClickEpisodeOrRelated = { selected ->
                                isEpisodeVisible.value = selected == 1
                                viewModel.onEvent(TvShowDetailScreenUiEvent.SimilarShows)
                            }) {
                            viewModel.onEvent(TvShowDetailScreenUiEvent.AddOrRemoveFromMyList)
                        }

                        if (isEpisodeVisible.value) {
                            SeasonToggle(
                                tvShow = tvShow,
                                episodes = state.value.episodes,
                                currentSeason = currentSeason.intValue
                            ) { selectedSeason ->
                                currentSeason.intValue = selectedSeason
                                viewModel.onEvent(
                                    TvShowDetailScreenUiEvent.SwitchEpisode(
                                        selectedSeason
                                    )
                                )
                            }

                            if (state.value.episodes.isEmpty()) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp),
                                    verticalArrangement = Arrangement.Top,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator()
                                }
                            } else {
                                state.value.episodes.forEach { episode ->
                                    EpisodeCell(episode) {

                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                        } else {
                            // similar shows
                            when {
                                state.value.isSimilarTvShowsLoading -> {
                                    Box(
                                        modifier = Modifier
                                            .height(500.dp)
                                            .fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }

                                }

                                else -> {
                                    if (state.value.similarTvShows.isEmpty()) {
                                        Box(
                                            modifier = Modifier
                                                .height(200.dp)
                                                .fillMaxWidth(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Oops! No similar tv show available",
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
                                                state.value.similarTvShows.take(state.value.similarTvShows.size / 2)
                                                    .forEach { tvShow ->
                                                        TvShowItem(
                                                            tvShow = tvShow,
                                                            navController = navController
                                                        )
                                                    }
                                            }

                                            Column {
                                                state.value.similarTvShows.takeLast(state.value.similarTvShows.size / 2)
                                                    .forEach { tvShow ->
                                                        TvShowItem(
                                                            tvShow = tvShow,
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








