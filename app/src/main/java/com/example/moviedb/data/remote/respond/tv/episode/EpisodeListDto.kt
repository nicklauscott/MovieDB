package com.example.moviedb.data.remote.respond.tv.episode

data class EpisodeListDto(
    val _id: String?,
    val air_date: String?,
    val episodes: List<EpisodeDto>?,
    val id: Int?,
    val name: String?,
    val overview: String?,
    val poster_path: String?,
    val season_number: Int?,
    val vote_average: Double?
)