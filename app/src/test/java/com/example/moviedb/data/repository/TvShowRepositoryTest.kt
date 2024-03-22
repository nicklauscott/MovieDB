package com.example.moviedb.data.repository

import com.example.moviedb.MainCoroutineRule
import com.example.moviedb.data.repository.fake.FakeTvShowRepository
import com.example.moviedb.domain.model.Episode
import com.example.moviedb.domain.model.TvShow
import com.example.moviedb.domain.repository.TvShowRepository
import com.example.moviedb.util.Resource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TvShowRepositoryTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var repository: TvShowRepository

    @Before
    fun setup() {
        val show  = TvShow(id = 1001, genre_ids = listOf(1, 2))
        val show2  = TvShow(id = 6278, genre_ids = listOf(1, 2))
        val season = mapOf(2 to listOf(Episode(show_id = show.id, season_number = 2, episode_number = 5)))
        repository = FakeTvShowRepository(tvShows = mutableListOf(show, show2), mutableMapOf(show.id to season))
    }

    @Test
    fun `getTvShows should return list of tv shows wrapped with resource class`() = runTest {
        repository.getTvShows(true, "", 1).collect { result ->
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            val show = result.data?.first { it.id == 1001 }
            assertThat(result.data).contains(show)
        }
    }

    @Test
    fun `getAShow should return the right show`() = runTest {
        val getShow = repository.getAShow(1001)
        assertThat(getShow.data?.id).isEqualTo(1001)
    }

    @Test
    fun `getTvEpisodesBySeason should return the right season if available`() = runTest {
        repository.getTvEpisodesBySeason(1001, 2).collect { result ->
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            assertThat(result.data?.first()?.show_id).isEqualTo(1001)
        }
    }

    @Test
    fun `getTvEpisodesBySeason should return error resource class if season is not available`() = runTest {
        repository.getTvEpisodesBySeason(1001, 3).collect { result ->
            assertThat(result).isInstanceOf(Resource.Error::class.java)
        }
    }

    @Test
    fun `getAnEpisode should return the right episode if available`() = runTest {
        repository.getAnEpisode(1001, 2, 5).collect { result ->
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            assertThat(result.data?.episode_number).isEqualTo(5)
        }
    }

    @Test
    fun `getAnEpisode should return error resource class if episode is not available`() = runTest {
        repository.getAnEpisode(1001, 2, 7).collect { result ->
            assertThat(result).isInstanceOf(Resource.Error::class.java)
        }
    }

    @Test
    fun `addShowToMyList should add show to my list if not already in my list`() = runTest {
        val result = repository.addShowToMyList(1001)
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun `addShowToMyList should not add show to my list if already in my list`() = runTest {
        repository.addShowToMyList(1001) // add to my list
        val result = repository.addShowToMyList(1001) // add to my list again
        assertThat(result).isEqualTo(false)
    }


    @Test
    fun `removeShowFromMyList should remove show from my list if it's in my list`() = runTest {
        repository.addShowToMyList(1001) // remove from my list
        val result = repository.removeShowFromMyList(1001) // remove from my list again
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun `removeShowFromMyList should not remove show from my list if it's not in my list`() = runTest {
        val result = repository.removeShowFromMyList(1001)
        assertThat(result).isEqualTo(false)
    }

    @Test
    fun `getSimilarTvShows should return list of related tv shows excluding current show`() = runTest {
        repository.getSimilarTvShows( 1001, // current show id
            listOf(1, 2)).collect { result ->
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            val show = result.data?.find { it.id == 1001 } // doesn't contain current show
            assertThat(show).isEqualTo(null)
        }
    }

    @Test
    fun `getSimilarTvShows should return list of related tv shows`() = runTest {
        repository.getSimilarTvShows( 1001, listOf(1, 2)).collect { result ->
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            val show = result.data?.find { it.id == 6278 } // show with same genre
            assertThat(result.data).contains(show)
        }
    }

}