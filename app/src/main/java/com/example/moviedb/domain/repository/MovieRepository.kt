package com.example.moviedb.domain.repository


import com.example.moviedb.domain.model.Movie
import com.example.moviedb.domain.model.TvShow
import com.example.moviedb.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>>

    suspend fun getAMovie(movieId: Int): Flow<Resource<Movie>>

    suspend fun getMoviesInMyList(): Flow<Resource<List<Movie>>>
}