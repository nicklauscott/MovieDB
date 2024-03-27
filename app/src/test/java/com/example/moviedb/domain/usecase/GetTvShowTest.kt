package com.example.moviedb.domain.usecase

import com.example.moviedb.MainCoroutineRule
import com.example.moviedb.data.repository.fake.FakeTvShowRepository
import com.example.moviedb.domain.model.Episode
import com.example.moviedb.domain.model.TvShow
import com.example.moviedb.domain.repository.TvShowRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetTvShowTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var getTvShow: GetTvShow

    @Before
    fun setup() {
        val show  = TvShow(id = 1001, genre_ids = listOf(1, 2))
        val season = mapOf(2 to listOf(Episode(show_id = show.id, season_number = 2, episode_number = 5)))
        val repository = FakeTvShowRepository(tvShows = mutableListOf(show), mutableMapOf(show.id to season))
        getTvShow = GetTvShow(repository)
    }

    @Test
    fun `getTvShow should return the right tvShow if available`() = runTest {
        getTvShow(1001) { result ->
            assertThat(result?.id).isEqualTo(1001)
        }
    }

    @Test
    fun `getTvShow should return null if the tv show is unavailable`() = runTest {
        getTvShow(7659) { result ->
            assertThat(result).isEqualTo(null)
        }
    }

}