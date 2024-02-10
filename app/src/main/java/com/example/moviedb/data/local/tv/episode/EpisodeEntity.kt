package com.example.moviedb.data.local.tv.episode

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "episode")
data class EpisodeEntity(
    @PrimaryKey val id: Int,

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