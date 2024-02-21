package com.example.moviedb.domain.usecase

import com.example.moviedb.domain.model.TvShow
import com.example.moviedb.domain.repository.TvShowRepository
import com.example.moviedb.util.Resource
import javax.inject.Inject

class GetTvShow @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(tvShowId: Int, result: (TvShow?) -> Unit) {
        when (val tvShow = tvShowRepository.getAShow(tvShowId)) {
            is Resource.Error -> {}
            is Resource.Loading -> {}
            is Resource.Success -> {
                result(tvShow.data)
            }
        }
    }
}