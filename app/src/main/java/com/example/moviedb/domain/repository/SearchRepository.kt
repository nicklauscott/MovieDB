package com.example.moviedb.domain.repository

import com.example.moviedb.domain.model.Search
import com.example.moviedb.util.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    suspend fun searchMoviesAndTvShows(
        path: String,
        localSearch: Boolean,
        searchQuery: String,
        adult: Boolean,
        page: Int
    ): Flow<Resource<List<Search>>>
}