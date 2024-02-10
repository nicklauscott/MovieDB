package com.example.moviedb.domain.repository


import com.example.moviedb.domain.model.Episode
import com.example.moviedb.domain.model.TvShow
import com.example.moviedb.util.Resource
import kotlinx.coroutines.flow.Flow

interface TvShowRepository {
    suspend fun getTvShows(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<TvShow>>>

    suspend fun getAShow(tvShowId: Int): Flow<Resource<TvShow>>

    suspend fun getTvEpisodesBySeason(tvShowId: Int, seasonNumber: Int): Flow<Resource<List<Episode>>>

    suspend fun getAnEpisode(tvShowId: Int, seasonNumber: Int, episodeId: Int): Flow<Resource<Episode>>

    suspend fun addShowToMyList(tvShowId: Int)

    suspend fun removeShowFromMyList(tvShowId: Int)

    suspend fun getShowsInMyList(): Flow<Resource<List<TvShow>>>
}