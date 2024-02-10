package com.example.moviedb.data.repository

import android.util.Log
import com.example.moviedb.data.local.MovieDatabase
import com.example.moviedb.data.mapper.toMovie
import com.example.moviedb.data.mapper.toMovieEntity
import com.example.moviedb.data.remote.MovieApi
import com.example.moviedb.domain.model.Movie
import com.example.moviedb.domain.repository.MovieRepository
import com.example.moviedb.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase
) : MovieRepository {
    override suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return flow {
            Log.d("TestViewModel-TestViewModel", "movieRepo - Requesting")
            emit(Resource.Loading(isLoading = true))
            val localMovieList = movieDatabase.movieDao.getAllMovieByCategory(category)

            val loadLocalMove = localMovieList.isNotEmpty() && !forceFetchFromRemote

            if (loadLocalMove) {
                emit(Resource.Success(localMovieList.map { movieEntity ->
                        movieEntity.toMovie(category)
                    }
                ))
                emit(Resource.Loading(isLoading = false))
                return@flow
            }

            val movieLIstFromRemote = try {
                movieApi.getMovies(category, page)
            } catch (ex: IOException) {
                ex.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            }
            catch (ex: HttpException) {
                ex.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            }
            catch (ex: Exception) {
                ex.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            }

            val movieEntities = movieLIstFromRemote.results.let {
                it.map { movieDto ->
                    movieDto.toMovieEntity(category) }
            }

            movieDatabase.movieDao.upsertMovieList(movieEntities)
            emit(Resource.Success(movieEntities.map { it.toMovie(category) }))
            emit(Resource.Loading(isLoading = false))
        }
    }

    override suspend fun getAMovie(movieId: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(isLoading = true))

            val movie = movieDatabase.movieDao.getMovieById(movieId)
            if (movie != null) {
                emit(Resource.Success(data = movie.toMovie("")))
                emit(Resource.Loading(isLoading = false))
                return@flow
            }
            emit(Resource.Error(message = "No such movie"))
            emit(Resource.Loading(isLoading = false))
        }
    }
}