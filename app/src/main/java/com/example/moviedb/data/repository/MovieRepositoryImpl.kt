package com.example.moviedb.data.repository

import com.example.moviedb.data.local.MovieDatabase
import com.example.moviedb.data.mapper.toMovie
import com.example.moviedb.data.mapper.toMovieEntity
import com.example.moviedb.data.remote.MovieApi
import com.example.moviedb.domain.model.Movie
import com.example.moviedb.domain.repository.MovieRepository
import com.example.moviedb.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
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
            emit(Resource.Loading(isLoading = true))
            val localMovieList = CoroutineScope(Dispatchers.IO).async { movieDatabase.movieDao.getAllMovieByCategory(category) }

            val loadLocalMove = localMovieList.await().isNotEmpty() && !forceFetchFromRemote

            if (loadLocalMove) {
                emit(Resource.Success(localMovieList.await().map { movieEntity ->
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

            CoroutineScope(Dispatchers.IO).launch { movieDatabase.movieDao.upsertMovieList(movieEntities) }
            emit(Resource.Success(movieEntities.map { it.toMovie(category) }))
            emit(Resource.Loading(isLoading = false))
        }
    }

    override suspend fun getAMovie(movieId: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(isLoading = true))

            val movieDetailDto = try {
                movieApi.getMovie(movieId)
            } catch (ex: IOException) {
                ex.printStackTrace()
                val movie = movieDatabase.movieDao.getMovieById(movieId)
                if (movie != null) {
                    emit(Resource.Success(data = movie.toMovie("")))
                    emit(Resource.Loading(isLoading = false))
                    return@flow
                }
                emit(Resource.Error(message = "Error loading movies"))
                emit(Resource.Loading(isLoading = false))
                return@flow
            }
            catch (ex: HttpException) {
                ex.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                emit(Resource.Loading(isLoading = false))
                return@flow
            }
            catch (ex: Exception) {
                ex.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                emit(Resource.Loading(isLoading = false))
                return@flow
            }

            val movie = movieDatabase.movieDao.getMovieById(movieId)
            CoroutineScope(Dispatchers.IO).launch {
                movieDatabase.movieDao.insertMovie(movieDetailDto.toMovieEntity().copy(inMyList = movie?.inMyList ?: false))
            }
            emit(Resource.Success(data = movieDetailDto.toMovieEntity().toMovie("").copy(inMyList = movie?.inMyList ?: false)))
            emit(Resource.Loading(isLoading = false))
        }
    }

    override suspend fun addMovieToMyList(movieId: Int): Boolean {
        CoroutineScope(Dispatchers.IO).launch {
            movieDatabase.movieDao.getMovieById(movieId)?.let { movieEntity ->
                movieDatabase.movieDao.insertMovie(movieEntity.copy(inMyList = true))
            }
        }.join()
        return true
    }

    override suspend fun removeMovieFromMyList(movieId: Int): Boolean {
        CoroutineScope(Dispatchers.IO).launch {
            movieDatabase.movieDao.getMovieById(movieId)?.let { movieEntity ->
                movieDatabase.movieDao.insertMovie(movieEntity.copy(inMyList = false))
            }
        }.join()
        return false
    }

    override suspend fun getMoviesInMyList(): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(isLoading = true))
            movieDatabase.movieDao.getAllMovieInMyList(true).collect { movieList ->
                emit(Resource.Success(movieList.map { it.toMovie("") }))
                emit(Resource.Loading(isLoading = false))
            }
        }
    }

    override suspend fun getSimilarMovies(movieId: Int): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(isLoading = true))

            val similarMovieListDto = try {
                movieApi.getSimilarMovies(movieId, 1)
            } catch (ex: IOException) {
                ex.printStackTrace()
                emit(Resource.Success(getOffLineSimilarMovies(movieId)))
                emit(Resource.Loading(isLoading = false))
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

            emit(Resource.Success(similarMovieListDto.results.map { it.toMovie() }))
            emit(Resource.Loading(isLoading = false))
        }
    }

    private suspend fun getOffLineSimilarMovies(movieId: Int): List<Movie> {
        return CoroutineScope(Dispatchers.IO).async {
            val genreIds = movieDatabase.movieDao.getMovieById(movieId)?.toMovie("")?.genre_ids ?: emptyList()
            movieDatabase.movieDao.getAllMovies().map { it.toMovie("") }.filter { movieItem ->
                genreIds.any { genre ->
                    movieItem.genre_ids.contains(genre)
                }
            }.filter { it.id != movieId }.shuffled().take(10)

        }.await()
    }
}