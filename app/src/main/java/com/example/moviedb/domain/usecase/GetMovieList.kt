package com.example.moviedb.domain.usecase

import com.example.moviedb.domain.model.Movie
import com.example.moviedb.domain.repository.MovieRepository
import com.example.moviedb.util.Category
import com.example.moviedb.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieList @Inject constructor(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(
        forceFetchFromRemote: Boolean, category: Category, page: Int,
        movies: (List<Movie>?) -> Unit
    ) {
        when (category) {
            Category.MyList -> {
                movieRepository.getMoviesInMyList().collect {
                    movies(it.data)
                }
            }
            else -> {
                movieRepository.getMovieList(forceFetchFromRemote, category.value, page).collect {
                    movies(it.data)
                }
            }
        }
    }

}

