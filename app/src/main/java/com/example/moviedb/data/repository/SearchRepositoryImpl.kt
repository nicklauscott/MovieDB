package com.example.moviedb.data.repository

import com.example.moviedb.data.local.MovieDatabase
import com.example.moviedb.data.mapper.toSearch
import com.example.moviedb.data.remote.SearchApi
import com.example.moviedb.domain.model.Search
import com.example.moviedb.domain.repository.SearchRepository
import com.example.moviedb.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchApi: SearchApi,
    private val movieDatabase: MovieDatabase
): SearchRepository {

    override suspend fun searchMoviesAndTvShows(
        path: String,
        localSearch: Boolean,
        searchQuery: String,
        adult: Boolean,
        page: Int
    ): Flow<Resource<List<Search>>> {
        return flow {
            emit(Resource.Loading(true))

            // get from database
            if (localSearch) {
                if (path == "multi") {
                    emit(Resource.Success(searchLocalMovies(searchQuery).await() + searchLocalTvShows(searchQuery).await().shuffled()))
                    emit(Resource.Loading(false))
                    return@flow
                }

                if (path == "tv") {
                    emit(Resource.Success(searchLocalTvShows(searchQuery).await()))
                    emit(Resource.Loading(false))
                    return@flow
                }

                emit(Resource.Success(searchLocalMovies(searchQuery).await()))
                emit(Resource.Loading(false))
                return@flow
            }

            // search web
            val searchLIstFromRemote = try {
                searchApi.searchMoviesAndTvShows(path, searchQuery, adult, page)
            } catch (ex: IOException) {
                ex.printStackTrace()
                emit(Resource.Error(message = "Error searching movies"))
                return@flow
            }
            catch (ex: HttpException) {
                ex.printStackTrace()
                emit(Resource.Error(message = "Error searching movies"))
                return@flow
            }
            catch (ex: Exception) {
                ex.printStackTrace()
                emit(Resource.Error(message = "Error searching movies"))
                return@flow
            }

            emit(Resource.Success(searchLIstFromRemote.results.map { it.toSearch() }.shuffled()))
            emit(Resource.Loading(false))
            return@flow

        }
    }

    private fun searchLocalMovies(searchQuery: String): Deferred<List<Search>> {
        return CoroutineScope(Dispatchers.IO).async{ movieDatabase.movieDao.searchMovieByName(searchQuery).map { it.toSearch() } }
    }

    private fun searchLocalTvShows(searchQuery: String): Deferred<List<Search>> {
        return CoroutineScope(Dispatchers.IO).async{ movieDatabase.tvDao.searchTvByName(searchQuery).map { it.toSearch() } }
    }
}