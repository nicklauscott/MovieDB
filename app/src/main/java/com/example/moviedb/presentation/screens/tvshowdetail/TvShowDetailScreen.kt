package com.example.moviedb.presentation.screens.tvshowdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.moviedb.presentation.component.DetailScreenTopBar
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
                // TODO
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

                    state.value.tvShow?.let {  tvShow ->

                        // show detail
                        TvShowDetailHeader(tvShow)

                        // show body
                        TvShowDetailBody(tvShow, isEpisodeVisible.value, onClickEpisodeOrRelated = { selected ->
                            isEpisodeVisible.value = selected == 1
                        }) {
                            viewModel.onEvent(TvShowDetailScreenUiEvent.AddOrRemoveFromMyList)
                        }

                        // season toggle
                        SeasonToggle(tvShow = tvShow,
                            episodes = state.value.episodes,
                            currentSeason = currentSeason.intValue) { selectedSeason ->
                            currentSeason.intValue = selectedSeason
                            viewModel.onEvent(TvShowDetailScreenUiEvent.SwitchEpisode(selectedSeason))
                        }

                        if (state.value.episodes.isEmpty()) {
                            Column(modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator()
                            }
                        }else {
                            state.value.episodes.forEach { episode ->
                                EpisodeCell(episode) {

                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                }
            }
        }
    }
}








