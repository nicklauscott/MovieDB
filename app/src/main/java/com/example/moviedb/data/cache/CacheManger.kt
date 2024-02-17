package com.example.moviedb.data.cache

import android.util.Log
import android.util.LruCache
import com.example.moviedb.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CacheManger {
    private val tvShowCache: LruCache<Int, TvShowModel>

    init {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 2
        tvShowCache = LruCache(cacheSize)
    }

    fun addToCache(key: Int, value: TvShowModel) {
        // remove and add new item if the item is already in cache
        if (tvShowCache.get(key) != null) {
            tvShowCache.remove(key)
            tvShowCache.put(key, value)
            return
        }

        tvShowCache.put(key, value)
    }

    suspend fun getFromCache(key: Int): TvShowModel? {
        return tvShowCache.get(key)
    }

    suspend fun getFlowFromCache(key: Int): Flow<Resource<TvShowModel?>> {
        return flow {
            emit(Resource.Loading(true))
            val value = tvShowCache.get(key)
            if (value != null) {
                emit(Resource.Success(value))
                emit(Resource.Loading(false))
                return@flow
            }
            emit(Resource.Error("data not found"))
            emit(Resource.Loading(false))
        }
    }
}