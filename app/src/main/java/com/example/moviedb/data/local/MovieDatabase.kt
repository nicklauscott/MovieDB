package com.example.moviedb.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviedb.data.local.movie.MovieDao
import com.example.moviedb.data.local.movie.MovieEntity
import com.example.moviedb.data.local.tv.TvDao
import com.example.moviedb.data.local.tv.TvEntity
import com.example.moviedb.data.local.tv.episode.EpisodeDao
import com.example.moviedb.data.local.tv.episode.EpisodeEntity

@Database(
    entities = [MovieEntity::class, TvEntity::class, EpisodeEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase: RoomDatabase() {

    abstract val movieDao: MovieDao

    abstract val tvDao: TvDao

    abstract val episodeDao: EpisodeDao

}