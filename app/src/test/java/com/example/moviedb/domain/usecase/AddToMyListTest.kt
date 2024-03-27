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

class AddToMyListTest {

    private lateinit var addToMyList: AddToMyList

    @Before
    fun setUp() {
        val movies = mutableListOf(mock<Movie>().apply {
            whenever(id).thenReturn(1001)
            whenever(inMyList).thenReturn(false)
        })
        val shows = mutableListOf(mock<TvShow>().apply {
            whenever(id).thenReturn(1001)
            whenever(inMyList).thenReturn(false)
        })
        val movieRepository = FakeMovieRepository(movies)
        val tvShowRepository = FakeTvShowRepository(shows)
        addToMyList = AddToMyList(movieRepository, tvShowRepository)
    }

    @Test
    fun `addToMyList should return true if the Movie was successfully added to my list`() = runTest { // movie
        val movie = mock<Movie>().apply { whenever(id).thenReturn(1001) }
        val result = addToMyList(movie)
        assertThat(result).isTrue()
    }

    @Test
    fun `addToMyList should return false if the Movie was not added to my list`() = runTest { // movie
        val movie = mock<Movie>().apply { whenever(id).thenReturn(1001) }
        addToMyList(movie)
        val result = addToMyList(movie)
        assertThat(result).isFalse()
    }

    @Test
    fun `addToMyList should return true if the TvShow was successfully added to my list`() = runTest { // TvShow
        val show = mock<TvShow>().apply { whenever(id).thenReturn(1001) }
        val result = addToMyList(show)
        assertThat(result).isTrue()
    }

    @Test
    fun `addToMyList should return false if the TvShow was not added to my list`() = runTest { // TvShow
        val show = mock<TvShow>().apply { whenever(id).thenReturn(1001) }
        addToMyList(show)
        val result = addToMyList(show)
        assertThat(result).isFalse()
    }
}