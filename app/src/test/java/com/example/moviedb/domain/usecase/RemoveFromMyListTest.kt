package com.example.moviedb.domain.usecase

import com.example.moviedb.data.repository.fake.FakeMovieRepository
import com.example.moviedb.data.repository.fake.FakeTvShowRepository
import com.example.moviedb.domain.model.Movie
import com.example.moviedb.domain.model.TvShow
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class RemoveFromMyListTest {

    private lateinit var removeFromMyList: RemoveFromMyList

    @Before
    fun setUp() {
        val movies = mutableListOf(mock<Movie>().apply {
            whenever(id).thenReturn(1001)
            whenever(inMyList).thenReturn(true)
        })
        val shows = mutableListOf(mock<TvShow>().apply {
            whenever(id).thenReturn(1001)
            whenever(inMyList).thenReturn(true)
        })
        val movieRepository = FakeMovieRepository(movies)
        val tvShowRepository = FakeTvShowRepository(shows)
        removeFromMyList = RemoveFromMyList(movieRepository, tvShowRepository)
    }

    @Test
    fun `removeFromMyList should return true if the Movie was successfully removed from my list`() = runTest { // movie
        val movie = mock<Movie>().apply { whenever(id).thenReturn(1001) }
        val result = removeFromMyList(movie)
        assertThat(result).isTrue()
    }

    @Test
    fun `removeFromMyList should return false if the Movie was not removed from my list`() = runTest { // movie
        val movie = mock<Movie>().apply { whenever(id).thenReturn(1001) }
        removeFromMyList(movie)
        val result = removeFromMyList(movie)
        assertThat(result).isFalse()
    }

    @Test
    fun `removeFromMyList should return true if the TvShow was successfully removed from my list`() = runTest { // TvShow
        val show = mock<TvShow>().apply { whenever(id).thenReturn(1001) }
        val result = removeFromMyList(show)
        assertThat(result).isTrue()
    }

    @Test
    fun `removeFromMyList should return false if the TvShow was not removed from my list`() = runTest { // TvShow
        val show = mock<TvShow>().apply { whenever(id).thenReturn(1001) }
        removeFromMyList(show)
        val result = removeFromMyList(show)
        assertThat(result).isFalse()
    }
}