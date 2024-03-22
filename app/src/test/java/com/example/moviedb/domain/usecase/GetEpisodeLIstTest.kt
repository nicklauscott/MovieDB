package com.example.moviedb.domain.usecase

import com.example.moviedb.data.repository.fake.FakeTvShowRepository
import com.example.moviedb.domain.model.Episode
import com.example.moviedb.domain.model.TvShow
import com.example.moviedb.util.Resource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetEpisodeLIstTest {

    private lateinit var getEpisodeLIst: GetEpisodeLIst

    @Before
    fun setup() {
        val show = mock<TvShow>().apply {
            whenever(id).thenReturn(1001)
        }
        val episode1 = mock<Episode>().apply {
            whenever(episode_number).thenReturn(1)
        }
        val episode2 = mock<Episode>().apply {
            whenever(episode_number).thenReturn(2)
        }
        val season = mapOf(2 to listOf(episode1, episode2))
        val repository = FakeTvShowRepository(tvShows = mutableListOf(show), seasons = mutableMapOf(show.id to season))
        getEpisodeLIst = GetEpisodeLIst(repository)
    }

    @Test
    fun `getEpisodeLIst should return the right season episode list`() = runTest {
        getEpisodeLIst(1001, 2).collect { result ->
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            assertThat(result.data?.last()?.episode_number).isEqualTo(2)
        }
    }

    @Test
    fun `getEpisodeLIst should return error when season episodes are not available`() = runTest {
        getEpisodeLIst(1001, 5).collect { result ->
            assertThat(result).isInstanceOf(Resource.Error::class.java)
        }
    }
}