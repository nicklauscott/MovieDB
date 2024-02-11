package com.example.moviedb.data.local.tv

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TvDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun upsertTvList(tvList: List<TvEntity>)

    @Query("SELECT * FROM tv_series WHERE id =:tvId")
    suspend fun getTvById(tvId: Int): TvEntity?

    @Query("SELECT * FROM tv_series WHERE category =:category")
    fun getAllTvByCategory(category: String): List<TvEntity>

    @Query("SELECT * FROM tv_series WHERE inMyList =:favorite")
    fun getAllTvInMyList(favorite: Boolean = true): Flow<List<TvEntity>>

    @Query("SELECT * FROM tv_series WHERE inMyList =:favorite")
    fun getAllTvInMyLists(favorite: Boolean = true): List<TvEntity>

    @Upsert
    suspend fun upsertTv(tv: TvEntity)

    @Query("SELECT * FROM tv_series WHERE name LIKE :searchQuery")
    fun searchTvByName(searchQuery: String): List<TvEntity>
}