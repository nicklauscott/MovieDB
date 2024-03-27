package com.example.moviedb.data.local.tv.episode

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.moviedb.data.local.MovieDatabase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class EpisodeDaoTest {

    private lateinit var database: MovieDatabase
    private lateinit var episodeDao: EpisodeDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDatabase::class.java
        ).allowMainThreadQueries().build()
        episodeDao = database.episodeDao
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun upsertEpisodeList() = runTest {
        val episode = listOf(
            EpisodeEntity(id = 2321, show_id = 1001, season_number = 5),
            EpisodeEntity(id = 7238, show_id = 1001, season_number = 5),
            EpisodeEntity(id = 4567, show_id = 1001, season_number = 3),
        )
        episodeDao.upsertEpisodeList(episode)
        val result = episodeDao.getEpisodeById(2321)
        assertThat(result).isEqualTo(episode[0])
    }

    @Test
    fun getEpisodeById() = runTest {
        val episode = listOf(
            EpisodeEntity(id = 2321, show_id = 1001, season_number = 5),
            EpisodeEntity(id = 7238, show_id = 1001, season_number = 5),
        )
        episodeDao.upsertEpisodeList(episode)
        val result = episodeDao.getEpisodeById(7238)
        assertThat(result).isEqualTo(episode[1])
    }

    @Test
    fun getEpisodeByShowAndSeason() = runTest {
        val episode = listOf(
            EpisodeEntity(id = 2321, show_id = 1001, season_number = 5),
            EpisodeEntity(id = 7238, show_id = 1001, season_number = 5),
            EpisodeEntity(id = 8089, show_id = 1001, season_number = 3),
        )
        episodeDao.upsertEpisodeList(episode)
        val result = episodeDao.getEpisodeByShowAndSeason(1001, 5)
        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun removeEpisodes() = runTest {
        val episode = EpisodeEntity(id = 2321, show_id = 1001)
        episodeDao.upsertEpisode(episode)
        episodeDao.removeEpisodes(1001)
        val result = episodeDao.getEpisodeById(2321)
        assertThat(result).isNull()
    }

}