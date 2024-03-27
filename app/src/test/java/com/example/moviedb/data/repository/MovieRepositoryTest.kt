package com.example.moviedb.data.repository


import com.example.moviedb.data.repository.fake.FakeMovieRepository
import com.example.moviedb.domain.model.Movie
import com.example.moviedb.domain.repository.MovieRepository
import com.example.moviedb.util.Resource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class MovieRepositoryTest {

    private lateinit var repository: MovieRepository

    @Before
    fun setup() {
        val moviesList = mutableListOf(
           mock<Movie>().apply {
               whenever(id).thenReturn(1001)
               whenever(genre_ids).thenReturn(listOf(1, 2, 7))
               whenever(inMyList).thenReturn(false)
               whenever(category).thenReturn("upcoming")
           },
           mock<Movie>().apply {
               whenever(id).thenReturn(1234)
               whenever(genre_ids).thenReturn(listOf(3, 5, 7))
               whenever(inMyList).thenReturn(false)
               whenever(category).thenReturn("upcoming")
           },
           mock<Movie>().apply {
               whenever(id).thenReturn(6562)
               whenever(genre_ids).thenReturn(listOf(6, 8, 9))
               whenever(inMyList).thenReturn(true)
               whenever(category).thenReturn("popular")
           },
           mock<Movie>().apply {
               whenever(id).thenReturn(9876)
               whenever(genre_ids).thenReturn(listOf(3, 6, 4))
               whenever(inMyList).thenReturn(true)
               whenever(category).thenReturn("popular")
           },
           mock<Movie>().apply {
               whenever(id).thenReturn(8273)
               whenever(genre_ids).thenReturn(listOf(3, 4, 7))
               whenever(inMyList).thenReturn(true)
               whenever(category).thenReturn("trending")
           },
       )
        repository = FakeMovieRepository(moviesList)
    }

    @Test
    fun `getMovieList should return resource class in the right order`() = runTest {
        val result = repository.getMovieList(false, "", -1).toList()
        assertThat(result[0]).isInstanceOf(Resource.Loading::class.java)
        assertThat(result[1]).isInstanceOf(Resource.Success::class.java)
        assertThat(result[2]).isInstanceOf(Resource.Loading::class.java)
    }

    @Test
    fun `getMovieList should return movies in the category specified`() = runTest {
        val result = repository.getMovieList(false, "upcoming", -1).toList()
        assertThat(result[1].data?.first()?.category).isEqualTo("upcoming")
    }

    @Test
    fun `getMoviesInMyList should return movies in my list`() = runTest {
        val result = repository.getMoviesInMyList().toList()
        assertThat(result[1].data?.size).isEqualTo(3)
    }

    @Test
    fun `getAMovie should return the right movie if available`() = runTest {
        val result = repository.getAMovie(1001).toList()
        assertThat(result[1].data?.id).isEqualTo(1001)
    }

    @Test
    fun `getAMovie should return error if the movie is not available`() = runTest {
        val result = repository.getAMovie(-1).toList()
        assertThat(result[1]).isInstanceOf(Resource.Error::class.java)
    }

    @Test
    fun `addMovieToMyList should return true if movie was successfully added to my list`() = runTest {
        val result = repository.addMovieToMyList(1001)
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun `addMovieToMyList should return false if movie was not successfully added to my list`() = runTest {
        repository.addMovieToMyList(1001)
        val result = repository.addMovieToMyList(1001)
        assertThat(result).isEqualTo(false)
    }


    @Test
    fun `removeMovieFromMyList should return true if movie was successfully removed from my list`() = runTest {
        val result = repository.removeMovieFromMyList(6562)
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun `removeMovieFromMyList should return false if movie was not successfully removed from my list`() = runTest {
        repository.removeMovieFromMyList(6562)
        val result = repository.removeMovieFromMyList(6562)
        assertThat(result).isEqualTo(false)
    }

    @Test
    fun `getSimilarMovies should return similar movies without the current movie`() = runTest {
        val result = repository.getSimilarMovies(1001).toList()
        assertThat(result[1].data?.find { it.id == 1001 }).isNull() // exclude current movie
        assertThat(result[1].data?.find { it.id == 8273 }).isNotNull() // include related movie
        assertThat(result[1].data?.find { it.id == 6562 }).isNull() // exclude unrelated movie
    }

}