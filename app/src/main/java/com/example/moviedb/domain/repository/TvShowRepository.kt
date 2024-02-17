package com.example.moviedb.domain.repository


import com.example.moviedb.domain.model.Episode
import com.example.moviedb.domain.model.TvShow
import com.example.moviedb.util.Resource
import kotlinx.coroutines.flow.Flow

interface TvShowRepository {

    suspend fun test(tvShowId: Int, seasonNumber: Int): Flow<Resource<List<Episode>>>
    suspend fun getTvShows(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<TvShow>>>
    suspend fun getAShow(tvShowId: Int): Resource<TvShow> // test
    suspend fun getTvEpisodesBySeason(tvShowId: Int, seasonNumber: Int): Resource<List<Episode>> // test

    suspend fun getAnEpisode(tvShowId: Int, seasonNumber: Int, episodeId: Int): Flow<Resource<Episode>>

    suspend fun addShowToMyList(tvShowId: Int): Boolean

    suspend fun removeShowFromMyList(tvShowId: Int): Boolean

    suspend fun getShowsInMyList(): Flow<Resource<List<TvShow>>>

    suspend fun getSimilarTvShows(tvShowId: Int): Flow<Resource<List<TvShow>>>
}