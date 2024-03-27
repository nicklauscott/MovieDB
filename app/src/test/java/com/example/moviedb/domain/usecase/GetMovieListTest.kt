package com.example.moviedb.domain.usecase

import com.example.moviedb.data.repository.fake.FakeMovieRepository
import com.example.moviedb.domain.model.Movie
import com.example.moviedb.util.Category
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/*
 -- Because if the way getMovieList is implemented i had to use the when expression
 -- to isolate and test the actual [Success Resource] class containing the data.
            when (result) {
                null -> {}
                else -> { assertThat(result.size).isEqualTo(3) }
            }
 */
class GetMovieListTest {

    private lateinit var getMovieList: GetMovieList
    @Before
    fun setUp() {
        val movies = mutableListOf(
            mock<Movie>().apply {
                whenever(id).thenReturn(1001)
                whenever(inMyList).thenReturn(true)
                whenever(category).thenReturn("popular")
            },
            mock<Movie>().apply {
                whenever(id).thenReturn(7483)
                whenever(inMyList).thenReturn(false)
                whenever(category).thenReturn("popular")
            },
            mock<Movie>().apply {
                whenever(id).thenReturn(9282)
                whenever(inMyList).thenReturn(false)
                whenever(category).thenReturn("upcoming")
            },
            mock<Movie>().apply {
                whenever(id).thenReturn(9238)
                whenever(inMyList).thenReturn(true)
                whenever(category).thenReturn("popular")
            },
        )
        val repository = FakeMovieRepository(movies)
        getMovieList = GetMovieList(repository)
    }

    @Test
    fun `getMovieList should return list of Movies`() = runTest {
        getMovieList(false, Category.Popular, -1) { result ->
            when (result) {
                null -> {}
                else -> { assertThat(result.size).isEqualTo(3) }
            }
        }
    }

    @Test
    fun `getMovieList should return list of Movies in my list if specified in category`() = runTest {
        getMovieList(false, Category.MyList, -1) { result ->
            when (result) {
                null -> {}
                else -> { assertThat(result.size).isEqualTo(2) }
            }
        }
    }

}