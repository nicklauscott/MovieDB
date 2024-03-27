package com.example.moviedb.data.local.tv.episode

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "episode")
data class EpisodeEntity(
    @PrimaryKey val id: Int,

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