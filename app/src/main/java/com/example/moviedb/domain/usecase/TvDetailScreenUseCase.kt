package com.example.moviedb.domain.usecase

import javax.inject.Inject

class TvDetailScreenUseCase @Inject constructor(
    val getTvShow: GetTvShow,
    val getEpisodeLIst: GetEpisodeLIst,
    val addToMyList: AddToMyList,
    val removeFromMyList: RemoveFromMyList,
    val getSimilarTvShowLIst: GetSimilarTvShowLIst
)