package com.example.moviedb.data.cache

import android.util.Log
import android.util.LruCache

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
}