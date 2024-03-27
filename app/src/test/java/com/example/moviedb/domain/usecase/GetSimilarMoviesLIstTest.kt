package com.example.moviedb.domain.usecase

import com.example.moviedb.data.repository.fake.FakeMovieRepository
import com.example.moviedb.domain.model.Movie
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetSimilarMoviesLIstTest {

    private lateinit var getSimilarMoviesLIst: GetSimilarMoviesLIst

    @Before
    fun setUp() {
        val movies = mutableListOf(
            mock<Movie>().apply {
                whenever(id).thenReturn(1001)
                whenever(genre_ids).thenReturn(listOf(3, 4, 5))
            },
            mock<Movie>().apply {
                whenever(id).thenReturn(6872)
                whenever(genre_ids).thenReturn(listOf(1, 4))
            }
        )
        val repository = FakeMovieRepository(movies)
        getSimilarMoviesLIst = GetSimilarMoviesLIst(repository)
    }

    @Test
    fun `getSimilarMoviesLIst should return the similar Movies without the current show`() = runTest {
        val result = getSimilarMoviesLIst(1001).toList()
        assertThat(result[1].data?.find { it.id == 1001 }).isNull()
        assertThat(result[1].data?.find { it.id == 6872 }).isNotNull()
    }

}