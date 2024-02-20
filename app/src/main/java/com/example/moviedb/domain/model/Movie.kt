package com.example.moviedb.domain.model


data class Movie(
    val id: Int,
    val category: String,
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    val inMyList: Boolean? = false,

    val runtime: Int = -1,
    val revenue: Int = -1,
    val budget: Int = -1,
    val status: String = "",
    val production_companies: String = "",
    val production_countries: String = "",
)
