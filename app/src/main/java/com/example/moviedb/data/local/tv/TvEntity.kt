package com.example.moviedb.data.local.tv

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tv_series")
data class TvEntity(
    @PrimaryKey
    val id: Int,

    val media_type: String = "",
    val backdrop_path: String = "",

    val name: String = "",
    val original_name: String = "",
    val genre_ids: String = "",
    val season_count: Int = 1,
    val adult: Boolean = false,
    val first_air_date: String = "",
    val origin_country: String = "",
    val original_language: String = "",
    val overview: String = "",

    val popularity: Double = 0.0,
    val poster_path: String = "",
    val vote_average: Double = 0.0,
    val vote_count: Int = -1,
    val category: String = "",

    val inMyList: Boolean = false

)