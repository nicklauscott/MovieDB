package com.example.moviedb.data.local.tv

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.moviedb.data.local.MovieDatabase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class TvDaoTest {

    private lateinit var database: MovieDatabase
    private lateinit var tvDao: TvDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDatabase::class.java
        ).allowMainThreadQueries().build()
        tvDao = database.tvDao
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertTvShow() = runTest {
        val show = TvEntity(id = 1001)
        tvDao.insertTv(show)
        val result = tvDao.getTvById(1001)
        assertThat(result).isNotNull()
    }

    @Test
    fun getTvShows() = runTest {
        val shows = listOf(TvEntity(id = 1001), TvEntity(id = 1343))
        tvDao.upsertTvList(shows)
        val result = tvDao.getAllShow()
        assertThat(result).contains(shows[0])
        assertThat(result).contains(shows[1])
    }

    @Test
    fun updateTvShow() = runTest {
        val show = TvEntity(id = 1001, inMyList = false)
        tvDao.insertTv(show)
        tvDao.insertTv(show.copy(inMyList = true))
        val result = tvDao.getTvById(1001)
        assertThat(result?.inMyList).isTrue()
    }

    @Test
    fun searchTvShows() = runTest {
        val shows = listOf(
            TvEntity(id = 1001, name = "The Game"),
            TvEntity(id = 1343, name = "Stranger Things")
        )
        tvDao.upsertTvList(shows)
        val result = tvDao.searchTvByName("Things")
        assertThat(result).contains(shows[1])
    }

    @Test
    fun getAllTvInMyList() = runTest {
        val shows = listOf(
            TvEntity(id = 1001, inMyList = false),
            TvEntity(id = 1343, inMyList = true)
        )
        tvDao.upsertTvList(shows)
        val result = tvDao.getAllTvInMyList().first()
        assertThat(result).doesNotContain(shows[0])
        assertThat(result).contains(shows[1])
    }

}