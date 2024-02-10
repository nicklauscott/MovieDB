package com.example.moviedb.data.local.tv.episode

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface EpisodeDao {

    @Upsert
    suspend fun upsertEpisodeList(episodeList: List<EpisodeEntity>)

    @Query("SELECT * FROM episode WHERE id =:episodeId")
    suspend fun getEpisodeById(episodeId: Int): EpisodeEntity?

    @Query("SELECT * FROM episode WHERE show_id =:showId AND season_number =:seasonNumber")
    fun getEpisodeByShowAndSeason(showId: Int, seasonNumber: Int): List<EpisodeEntity>

    @Upsert
    suspend fun upsertEpisode(episode: EpisodeEntity)

    @Query("DELETE FROM episode WHERE show_id =:showId")
    suspend fun removeEpisodes(showId: Int)
}