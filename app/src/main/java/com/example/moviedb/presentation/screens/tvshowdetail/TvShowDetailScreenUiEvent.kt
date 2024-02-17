package com.example.moviedb.presentation.screens.tvshowdetail

sealed interface TvShowDetailScreenUiEvent {
    object AddOrRemoveFromMyList: TvShowDetailScreenUiEvent
    data class SwitchEpisode(val season: Int): TvShowDetailScreenUiEvent
    object SimilarShows: TvShowDetailScreenUiEvent
}