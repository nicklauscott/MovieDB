package com.example.moviedb.presentation.screens.search


sealed  interface SearchUiEvent {
    data class Search(val searchQuery: String): SearchUiEvent
    data class ChangeType(val type: Type): SearchUiEvent
    object ToggleSafeSearch: SearchUiEvent
    object ToggleLocalSearch: SearchUiEvent
    object Paginate: SearchUiEvent
}