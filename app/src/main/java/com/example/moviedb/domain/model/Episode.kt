package com.example.moviedb.domain.model

data class Episode(
    val id: Int,

    val name: String,
    val overview: String,
    val air_date: String,
    val season_number: Int,
    val episode_number: Int,
    val show_id: Int,

    val episode_type: String,
    val runtime: Int,
    val still_path: String,
)