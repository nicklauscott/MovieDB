package com.example.moviedb.domain.model

data class Episode(
    val id: Int = -1,

    val name: String = "",
    val overview: String = "",
    val air_date: String = "",
    val season_number: Int = -1,
    val episode_number: Int = -1,
    val show_id: Int = -1,

    val episode_type: String = "",
    val runtime: Int = -1,
    val still_path: String = "",
)