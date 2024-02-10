package com.example.moviedb.domain.usecase

import javax.inject.Inject

class HomeScreenUseCase @Inject constructor(
    val getMovieList: GetMovieList,
    val getTvShowList: GetTvShowList
)