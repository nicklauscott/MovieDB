package com.example.moviedb.domain.usecase

import com.example.moviedb.data.repository.fake.FakeMovieRepository
import com.example.moviedb.domain.model.Movie
import com.example.moviedb.util.Resource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetMovieTest {

    private lateinit var getMovie: GetMovie
    @Before
    fun setUp() {
        val movies = mutableListOf(mock<Movie>().apply {
            whenever(id).thenReturn(1001)
            whenever(inMyList).thenReturn(false)
        })
        val repository = FakeMovieRepository(movies)
        getMovie = GetMovie(repository)
    }

    @Test
    fun `getTvShow should return the right movie if available`() = runTest {
        val result = getMovie(1001).toList()
        assertThat(result[1].data?.id).isEqualTo(1001)
    }

    @Test
    fun `getTvShow should return error if the movie is unavailable`() = runTest {
        val result = getMovie(5643).toList()
        assertThat(result[1]).isInstanceOf(Resource.Error::class.java)
    }
}