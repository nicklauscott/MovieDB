package com.example.moviedb.domain.usecase

import com.example.moviedb.domain.model.Episode
import com.example.moviedb.domain.repository.TvShowRepository
import com.example.moviedb.util.Resource
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

}