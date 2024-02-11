package com.example.moviedb.presentation.screens.homescreen

import com.example.moviedb.util.Category


sealed interface HomeScreenUiEvent {
    data class Paginate(val category: Category) : HomeScreenUiEvent
    data class SwitchCategory(val category: Category) : HomeScreenUiEvent
    object Navigate : HomeScreenUiEvent
}