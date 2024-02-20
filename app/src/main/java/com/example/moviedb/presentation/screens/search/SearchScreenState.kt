package com.example.moviedb.presentation.screens.search

import com.example.moviedb.domain.model.Search

data class SearchScreenState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val searchList: List<Search> = emptyList(),
    val type: Type = Type.Both,
    val safeSearch: Boolean = true,
    val localSearch: Boolean = false,
    val searchPage: Int = 1,
    val message: String? = null
)

enum class Type(val pathName: String) {
    Movies("movie"),
    TvShows("tv"),
    Both("multi"),
}
