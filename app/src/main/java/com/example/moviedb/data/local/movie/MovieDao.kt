package com.example.moviedb.data.local.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Upsert
    suspend fun upsertMovieList(movieList: List<MovieEntity>)

    @Query("SELECT * FROM movie WHERE id =:movieId")
    suspend fun getMovieById(movieId: Int): MovieEntity?

    @Query("SELECT * FROM movie WHERE category =:category")
    fun getAllMovieByCategory(category: String): List<MovieEntity>

    @Query("SELECt * FROM movie")
    fun getAllMovies(): List<MovieEntity>

    @Query("SELECT * FROM movie WHERE inMyList =:favorite")
    fun getAllMovieInMyList(favorite: Boolean = true): Flow<List<MovieEntity>>

    @Upsert
    suspend fun upsertMovie(movieEntity: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movieEntity: MovieEntity)

    @Query("SELECT * FROM movie WHERE title LIKE '%' || :searchQuery || '%'")
    fun searchMovieByName(searchQuery: String): List<MovieEntity>

}