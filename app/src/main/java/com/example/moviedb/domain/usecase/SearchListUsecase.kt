package com.example.moviedb.domain.usecase

import com.example.moviedb.domain.model.Search
import com.example.moviedb.domain.repository.SearchRepository
import com.example.moviedb.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchListUsecase @Inject constructor (
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(
        path: String, localSearch: Boolean, searchQuery: String, adult: Boolean, page: Int
    ): Flow<Resource<List<Search>>> {
        return searchRepository.searchMoviesAndTvShows(path, localSearch, searchQuery, adult, page)
    }
}