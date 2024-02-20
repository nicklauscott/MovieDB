package com.example.moviedb.presentation.screens.moviedetail

sealed interface MovieDetailUiEvent {
    object AddOrRemoveFromMyList: MovieDetailUiEvent
}