package com.example.moviedb.presentation.screens.homescreen

import com.example.moviedb.domain.model.Movie
import com.example.moviedb.domain.model.TvShow
import com.example.moviedb.util.Category

data class HomeScreenState(
    val isLoading: Boolean = false,

    val movieListPage: Int = 1,
    val tvListPage: Int = 1,

    val category: Category = Category.Popular,

    val isMovieListScreen: Boolean = true,

    val movieList: List<Movie> = emptyList(),
    val tvShowList: List<TvShow> = emptyList()
)
