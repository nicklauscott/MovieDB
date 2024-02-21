package com.example.moviedb.domain.usecase

import com.example.moviedb.domain.model.TvShow
import com.example.moviedb.domain.repository.TvShowRepository
import com.example.moviedb.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSimilarTvShowLIst @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(tvShowId: Int, genre: List<Int>): Flow<Resource<List<TvShow>>>{
        return tvShowRepository.getSimilarTvShows(tvShowId, genre)
    }

}