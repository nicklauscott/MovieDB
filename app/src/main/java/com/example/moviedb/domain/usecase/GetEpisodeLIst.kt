package com.example.moviedb.domain.usecase

import com.example.moviedb.domain.model.Episode
import com.example.moviedb.domain.repository.TvShowRepository
import com.example.moviedb.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEpisodeLIst @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {

    suspend operator fun invoke(tvShowId: Int, seasonNUmber: Int): List<Episode>?{
        return when (val episodes = tvShowRepository.getTvEpisodesBySeason(tvShowId, seasonNUmber)) {
            is Resource.Success -> episodes.data
            else -> null
        }
    }

    suspend fun test(tvShowId: Int, seasonNUmber: Int): Flow<Resource<List<Episode>>> {
        return tvShowRepository.test(tvShowId, seasonNUmber)
    }

}