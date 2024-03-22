package com.example.moviedb.data.local.movie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class MovieEntity(
    @PrimaryKey val id: Int,

    val category: String = "",

    val adult: Boolean = false,
    val backdrop_path: String = "",
    val genre_ids: String = "",
    val original_language: String = "",
    val original_title: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    val poster_path: String = "",
    val release_date: String = "",
    val title: String = "",
    val video: Boolean = false,
    val vote_average: Double = 0.0,
    val vote_count: Int = -1,
    val inMyList: Boolean = false,

    val runtime: Int = -1,
    val revenue: Int = -1,
    val budget: Int = -1,
    val status: String = "",
    val production_companies: String = "",
    val production_countries: String = "",
)
