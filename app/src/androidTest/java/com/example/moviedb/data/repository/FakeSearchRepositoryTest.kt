package com.example.moviedb.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.moviedb.data.local.MovieDatabase
import com.example.moviedb.data.local.movie.MovieEntity
import com.example.moviedb.data.local.tv.TvEntity
import com.example.moviedb.data.repository.fake.FakeSearchRepository
import com.example.moviedb.domain.repository.SearchRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class FakeSearchRepositoryTest {

    private lateinit var repository: SearchRepository
    private lateinit var database: MovieDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDatabase::class.java
        ).allowMainThreadQueries().build()
        repository = FakeSearchRepository(database)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun searchMoviesAndTvShows_shouldReturnMovies_ifPathIsMovie() = runTest {
        val movies = listOf(
            MovieEntity(id = 1001, title = "Avatar"),
            MovieEntity(id = 8888, title = "Superman")
        )
        val shows = listOf(
            TvEntity(id = 2324, name = "Avatar"),
            TvEntity(id = 2222, name = "Superman")
        )
        database.tvDao.upsertTvList(shows)
        database.movieDao.upsertMovieList(movies)
        val result = repository.searchMoviesAndTvShows(
            path = "movie", localSearch = false, searchQuery = "man", adult = false, page = 0).toList()
        assertThat(result[1].data?.find { it.id ==  8888}).isNotNull()
        assertThat(result[1].data?.find { it.id ==  2222}).isNull()
    }

    @Test
    fun searchMoviesAndTvShows_shouldReturnTvShows_ifPathIsTv() = runTest {
        val movies = listOf(
            MovieEntity(id = 1001, title = "Avatar"),
            MovieEntity(id = 8888, title = "Superman")
        )
        val shows = listOf(
            TvEntity(id = 2324, name = "Avatar"),
            TvEntity(id = 2222, name = "Superman")
        )
        database.tvDao.upsertTvList(shows)
        database.movieDao.upsertMovieList(movies)
        val result = repository.searchMoviesAndTvShows(
            path = "tv", localSearch = false, searchQuery = "man", adult = false, page = 0).toList()
        assertThat(result[1].data?.find { it.id ==  8888}).isNull()
        assertThat(result[1].data?.find { it.id ==  2222}).isNotNull()
    }

    @Test
    fun searchMoviesAndTvShows_shouldReturnMoviesAndTvShows_ifPathIsMulti() = runTest {
        val movies = listOf(
            MovieEntity(id = 1001, title = "Avatar"),
            MovieEntity(id = 8888, title = "Superman")
        )
        val shows = listOf(
            TvEntity(id = 2324, name = "Avatar"),
            TvEntity(id = 2222, name = "Superman")
        )
        database.tvDao.upsertTvList(shows)
        database.movieDao.upsertMovieList(movies)
        val result = repository.searchMoviesAndTvShows(
            path = "multi", localSearch = false, searchQuery = "man", adult = false, page = 0).toList()
        assertThat(result[1].data?.find { it.id ==  8888}).isNotNull()
        assertThat(result[1].data?.find { it.id ==  2222}).isNotNull()
        assertThat(result[1].data?.size).isEqualTo(2)
    }

}