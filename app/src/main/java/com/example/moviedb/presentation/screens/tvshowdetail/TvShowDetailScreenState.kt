package com.example.moviedb.presentation.screens.tvshowdetail

import com.example.moviedb.domain.model.Episode
import com.example.moviedb.domain.model.TvShow

data class TvShowDetailScreenState(
    val tvShow: TvShow? = null,

    val isEpisodeLoading: Boolean = false,
    val episodes: List<Episode> = emptyList()
)
