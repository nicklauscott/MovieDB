package com.example.moviedb.domain.usecase

import javax.inject.Inject

class MovieDetailScreenUseCase @Inject constructor(
    val getMovie: GetMovie,
    val getSimilarMoviesLIst: GetSimilarMoviesLIst,
    val addToMyList: AddToMyList,
    val removeFromMyList: RemoveFromMyList
)