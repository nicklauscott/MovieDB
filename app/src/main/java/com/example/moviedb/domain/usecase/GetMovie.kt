package com.example.moviedb.domain.usecase

import com.example.moviedb.domain.model.Movie
import com.example.moviedb.domain.repository.MovieRepository
import com.example.moviedb.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovie @Inject constructor(
    val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): Flow<Resource<Movie>> {
        return movieRepository.getAMovie(movieId)
    }
}