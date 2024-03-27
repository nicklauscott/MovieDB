package com.example.moviedb.data.repository.fake

import com.example.moviedb.data.local.MovieDatabase
import com.example.moviedb.data.mapper.toSearch
import com.example.moviedb.domain.model.Search
import com.example.moviedb.domain.repository.SearchRepository
import com.example.moviedb.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeSearchRepository(
    private val database: MovieDatabase
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

            if (path == "multi") {
                emit(Resource.Success(searchLocalMovies(searchQuery) + searchLocalTvShows(searchQuery).shuffled()))
                emit(Resource.Loading(false))
                return@flow
            }

            if (path == "tv") {
                emit(Resource.Success(searchLocalTvShows(searchQuery)))
                emit(Resource.Loading(false))
                return@flow
            }

            emit(Resource.Success(searchLocalMovies(searchQuery)))
            emit(Resource.Loading(false))
        }
    }

    private fun searchLocalMovies(query: String) =
        database.movieDao.searchMovieByName(query).map { it.toSearch() }

    private fun searchLocalTvShows(query: String) =
        database.tvDao.searchTvByName(query).map { it.toSearch() }
}