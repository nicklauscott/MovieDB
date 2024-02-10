package com.example.moviedb.data.mapper

import com.example.moviedb.data.local.tv.episode.EpisodeEntity
import com.example.moviedb.data.remote.respond.tv.episode.EpisodeDto
import com.example.moviedb.domain.model.Episode

fun EpisodeDto.toEpisodeEntity(): EpisodeEntity =
    EpisodeEntity(
        air_date = air_date ?: "",
        episode_number = episode_number ?: 0,
        episode_type = episode_type ?: "",
        id = id ?: 0,
        name = name ?: "",
        overview = overview ?: "",
        runtime = runtime ?: 0,
        season_number = season_number ?: 0,
        show_id = show_id ?: 0,
        still_path = still_path ?: ""
    )


fun EpisodeEntity.toEpisode(): Episode =
    Episode(
        air_date = air_date,
        episode_number = episode_number,
        episode_type = episode_type,
        id = id,
        name = name,
        overview = overview,
        runtime = runtime,
        season_number = season_number,
        show_id = show_id,
        still_path = still_path
    )