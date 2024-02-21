package com.example.moviedb.domain.usecase

import com.example.moviedb.domain.model.Movie
import com.example.moviedb.domain.repository.MovieRepository
import com.example.moviedb.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSimilarMoviesLIst @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(tvShowId: Int): Flow<Resource<List<Movie>>>{
        return movieRepository.getSimilarMovies(tvShowId)
    }

}