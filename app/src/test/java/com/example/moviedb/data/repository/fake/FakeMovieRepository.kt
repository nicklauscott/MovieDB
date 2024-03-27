package com.example.moviedb.data.repository.fake

import com.example.moviedb.domain.model.Movie
import com.example.moviedb.domain.repository.MovieRepository
import com.example.moviedb.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeMovieRepository(
    private val movies: MutableList<Movie>
): MovieRepository {

    override suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            if (category.isBlank()) {
                emit(Resource.Success(movies))
                emit(Resource.Loading(false))
                return@flow
            }

            emit(Resource.Success(movies.filter { it.category == category }))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getAMovie(movieId: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(true))
            val movie = movies.find { it.id == movieId }
            if (movie != null) {
                emit(Resource.Success(movie))
                emit(Resource.Loading(false))
                return@flow
            }
            emit(Resource.Error(""))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun addMovieToMyList(movieId: Int): Boolean {
        val movie = movies.find { it.id == movieId }
        if (movie?.inMyList == false) {
            movies.removeIf { it.id == movieId }
            movies.add(mock<Movie>().apply {
                whenever(id).thenReturn(movieId)
                whenever(inMyList).thenReturn(true)
            })
            return true
        }
        return false
    }

    override suspend fun removeMovieFromMyList(movieId: Int): Boolean {
        val movie = movies.find { it.id == movieId }
        if (movie?.inMyList == true) {
            movies.removeIf { it.id == movieId }
            movies.add(mock<Movie>().apply {
                whenever(id).thenReturn(movieId)
                whenever(inMyList).thenReturn(false)
            })
            return true
        }
        return false
    }

    override suspend fun getMoviesInMyList(): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            emit(Resource.Success(movies.filter { it.inMyList == true }))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getSimilarMovies(movieId: Int): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))
            val genres = movies.find { it.id == movieId }?.genre_ids ?: listOf(-1, -1)
            val similarMovies = movies.filter { movie ->
                genres.any { genreId ->
                    movie.genre_ids.contains(genreId)
                }
            }.filter { it.id != movieId }
            emit(Resource.Success(similarMovies))
            emit(Resource.Loading(false))
        }
    }
}