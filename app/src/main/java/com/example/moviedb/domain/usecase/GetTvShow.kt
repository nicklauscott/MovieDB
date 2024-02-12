package com.example.moviedb.domain.usecase

import android.util.Log
import com.example.moviedb.domain.model.Episode
import com.example.moviedb.domain.model.TvShow
import com.example.moviedb.domain.repository.TvShowRepository
import com.example.moviedb.util.Resource
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class GetTvShowDetail @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {

    suspend operator fun invoke(tvShowId: Int, result: (TvShow?, List<Episode>?) -> Unit) {
        when (val tvShow = tvShowRepository.getAShow(tvShowId)) {
            is Resource.Error -> {}
            is Resource.Loading -> {}
            is Resource.Success -> {
                val episodes = tvShowRepository.getTvEpisodesBySeason(tvShowId, 1)
                result(tvShow.data, episodes.data)
            }
        }
    }
}