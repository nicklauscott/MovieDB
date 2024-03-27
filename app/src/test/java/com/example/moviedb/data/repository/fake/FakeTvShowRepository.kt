package com.example.moviedb.data.repository.fake

import com.example.moviedb.domain.model.Episode
import com.example.moviedb.domain.model.TvShow
import com.example.moviedb.domain.repository.TvShowRepository
import com.example.moviedb.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeTvShowRepository(
    private val tvShows:  MutableList<TvShow> = mutableListOf(),
    private val seasons:  MutableMap<Int, Map<Int, List<Episode>>> = mutableMapOf()
): TvShowRepository {

    override suspend fun getTvShows(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<TvShow>>> {
        return flow {
            emit(Resource.Success(tvShows))
        }
    }

    override suspend fun getTvEpisodesBySeason(
        tvShowId: Int,
        seasonNumber: Int
    ): Flow<Resource<List<Episode>>> {
        return flow {
            val show = seasons[tvShowId]
            val season = show?.get(seasonNumber)
            if (season != null) {
                emit(Resource.Success(season.sortedBy { it.episode_number }))
                return@flow
            }
            emit(Resource.Error(""))
        }
    }

    override suspend fun getAShow(tvShowId: Int): Resource<TvShow> {
        return Resource.Success(tvShows.find { it.id == tvShowId })
    }

    override suspend fun getAnEpisode(
        tvShowId: Int,
        seasonNumber: Int,
        episodeId: Int
    ): Flow<Resource<Episode>> {
        return flow {
            val show = seasons[tvShowId]
            val season = show?.get(seasonNumber)
            if (season != null) {
                val episode = season.find { it.episode_number == episodeId }
                if (episode != null) {
                    emit(Resource.Success(episode))
                    return@flow
                }
            }
            emit(Resource.Error(""))
        }
    }

    override suspend fun addShowToMyList(tvShowId: Int): Boolean {
        val show = tvShows.find { it.id == tvShowId }
        if (show != null && show.inMyList == false) {
            tvShows.removeIf { it.id == tvShowId }.also {  update ->
                if (update) tvShows.add(
                    mock<TvShow>().apply {
                        whenever(id).thenReturn(tvShowId)
                        whenever(inMyList).thenReturn(true)
                    }
                )
                return true
            }
        }
        return false
    }

    override suspend fun removeShowFromMyList(tvShowId: Int): Boolean {
        val show = tvShows.find { it.id == tvShowId }
        if (show?.inMyList == true) {
            tvShows.removeIf { it.id == tvShowId }
            tvShows.add(mock<TvShow>().apply {
                whenever(id).thenReturn(tvShowId)
                whenever(inMyList).thenReturn(false)
            })
            return true
        }
        return false
    }

    override suspend fun getShowsInMyList(): Flow<Resource<List<TvShow>>> {
        return flow {
            emit(Resource.Success(tvShows.filter { it.inMyList == true }))
        }
    }

    override suspend fun getSimilarTvShows(
        tvShowId: Int,
        genres: List<Int>
    ): Flow<Resource<List<TvShow>>> {
        return flow {
            val similarShows = tvShows.filter { show ->
                genres.any { genreId ->
                    show.genre_ids.contains(genreId)
                }
            }.filter { it.id != tvShowId }

            emit(Resource.Success(similarShows))
        }
    }

}