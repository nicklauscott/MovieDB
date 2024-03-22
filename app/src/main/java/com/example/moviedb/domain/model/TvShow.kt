package com.example.moviedb.domain.model

data class TvShow(
    val id: Int = -1,
    val adult: Boolean = false,
    val backdrop_path: String = "",
    val first_air_date: String = "",
    val genre_ids: List<Int> = emptyList(),
    val season_count: Int = -1,
    val media_type: String = "",
    val name: String = "",
    val origin_country: List<String> = emptyList(),
    val original_language: String = "",
    val original_name: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    val poster_path: String = "",
    val vote_average: Double = 0.0,
    val vote_count: Int = -1,
    val inMyList: Boolean? = false,
    val category: String = ""
)
