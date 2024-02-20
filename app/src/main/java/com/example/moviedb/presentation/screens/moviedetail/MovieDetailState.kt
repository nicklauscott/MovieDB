package com.example.moviedb.presentation.screens.moviedetail

import com.example.moviedb.domain.model.Movie

data class MovieDetailState(
    val isLoading: Boolean = false,
    val movie: Movie? = null,

    val isSimilarMoviesLoading: Boolean = false,
    val similarMovies: List<Movie> = emptyList()
)
