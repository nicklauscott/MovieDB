package com.example.moviedb.presentation.screens.tvshowdetail

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.moviedb.domain.model.Episode
import com.example.moviedb.domain.model.TvShow
import com.example.moviedb.presentation.component.DetailScreenTopBar
import com.example.moviedb.presentation.component.MatureRating
import com.example.moviedb.presentation.screens.tvshowdetail.component.SeasonSpinner
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
                    }

                    Log.d("TestViewModel-TestViewModel", "${state.value.tvShow}")

                }
            }
        }
    }
}






