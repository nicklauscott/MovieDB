package com.example.moviedb.domain.usecase

import com.example.moviedb.domain.model.Movie
import com.example.moviedb.domain.model.TvShow
import com.example.moviedb.domain.repository.MovieRepository
import com.example.moviedb.domain.repository.TvShowRepository
import javax.inject.Inject

class AddToMyList @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(movie: Movie): Boolean {
        return movieRepository.addMovieToMyList(movie.id)
    }

    suspend operator fun invoke(tvShow: TvShow): Boolean {
        return tvShowRepository.addShowToMyList(tvShow.id)
    }
}