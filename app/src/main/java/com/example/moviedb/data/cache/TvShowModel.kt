package com.example.moviedb.data.cache

import com.example.moviedb.data.local.tv.TvEntity
import com.example.moviedb.data.local.tv.episode.EpisodeEntity
import com.example.moviedb.data.remote.respond.tv.TvDto

data class TvShowModel(
    val id: Int,
    val tvEntity: TvEntity,
    val numberOfSeason: Int,
    val seasons: List<Season>
)

data class Season(
    val showId: Int,
    val overView: String,
    val seasonNumber: Int,
    val numberOfEpisode: Int,
    val episodes: List<EpisodeEntity>
)
