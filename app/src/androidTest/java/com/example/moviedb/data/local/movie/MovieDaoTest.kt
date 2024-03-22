package com.example.moviedb.data.local.movie

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.moviedb.data.local.MovieDatabase
import com.example.moviedb.data.local.tv.TvEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class MovieDaoTest {

    private lateinit var database: MovieDatabase
    private lateinit var movieDao: MovieDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDatabase::class.java
        ).allowMainThreadQueries().build()
        movieDao = database.movieDao
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertMovie() = runTest {
        val movie = MovieEntity(id = 1001)
        movieDao.insertMovie(movie)
        val result = movieDao.getMovieById(1001)
        assertThat(result).isNotNull()
    }

    @Test
    fun getMovies() = runTest {
        val movies = listOf(
            MovieEntity(id = 1001, inMyList = true),
            MovieEntity(id = 6754, inMyList = false)
        )
        movieDao.upsertMovieList(movies)
        val result = movieDao.getAllMovies()
        assertThat(result).contains(movies[0])
        assertThat(result).contains(movies[1])
    }

    @Test
    fun updateMovie() = runTest {
        val movie = MovieEntity(id = 1001, inMyList = false)
        movieDao.insertMovie(movie)
        movieDao.upsertMovie(movie.copy(inMyList = true))
        val result = movieDao.getMovieById(1001)
        assertThat(result?.inMyList).isTrue()
    }

    @Test
    fun searchMovies() = runTest {
        val movies = listOf(
            MovieEntity(id = 1001, title = "Avatar"),
            MovieEntity(id = 6754, title = "Superman")
        )
        movieDao.upsertMovieList(movies)
        val result = movieDao.searchMovieByName("superman")
        assertThat(result).contains(movies[1])
    }

    @Test
    fun getAllMovieInMyList() = runTest {
        val movies = listOf(
            MovieEntity(id = 1001, inMyList = false),
            MovieEntity(id = 6754, inMyList = true)
        )
        movieDao.upsertMovieList(movies)
        val result = movieDao.getAllMovieInMyList().first()
        assertThat(result).doesNotContain(movies[0])
        assertThat(result).contains(movies[1])
    }

}