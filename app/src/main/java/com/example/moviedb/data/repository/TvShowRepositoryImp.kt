package com.example.moviedb.data.repository

import android.util.Log
import com.example.moviedb.data.cache.CacheManger
import com.example.moviedb.data.cache.Season
import com.example.moviedb.data.cache.TvShowModel
import com.example.moviedb.data.local.MovieDatabase
import com.example.moviedb.data.local.tv.TvEntity
import com.example.moviedb.data.mapper.toEpisode
import com.example.moviedb.data.mapper.toEpisodeEntity
import com.example.moviedb.data.mapper.toTvEntity
import com.example.moviedb.data.mapper.toTvShow
import com.example.moviedb.data.remote.TvApi
import com.example.moviedb.data.remote.respond.tv.tvdetail.TvShowDetailDto
import com.example.moviedb.domain.model.Episode
import com.example.moviedb.domain.model.TvShow
import com.example.moviedb.domain.repository.TvShowRepository
import com.example.moviedb.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class TvShowRepositoryImp @Inject constructor(
    private val tvApi: TvApi,
    private val movieDatabase: MovieDatabase,
    private val cacheManager: CacheManger
): TvShowRepository {

    override suspend fun getTvShows(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<TvShow>>> {
        return flow {
            emit(Resource.Loading(isLoading = true))
            val localTvList = CoroutineScope(Dispatchers.Default).async { movieDatabase.tvDao.getAllTvByCategory(category) }

            val loadLocalMove = localTvList.await().isNotEmpty() && !forceFetchFromRemote

            if (loadLocalMove) {
                emit(Resource.Success(localTvList.await().map { tvEntity ->
                    tvEntity.toTvShow(category)
                }
                ))
                emit(Resource.Loading(isLoading = false))
                return@flow
            }

            val movieLIstFromRemote = try {
                tvApi.getTvShows(category, page)
            } catch (ex: IOException) {
                ex.printStackTrace()
                emit(Resource.Error(message = "Error loading shows"))
                return@flow
            }
            catch (ex: HttpException) {
                ex.printStackTrace()
                emit(Resource.Error(message = "Error loading shows"))
                return@flow
            }
            catch (ex: Exception) {
                ex.printStackTrace()
                emit(Resource.Error(message = "Error loading shows"))
                return@flow
            }

            val movieEntities = movieLIstFromRemote.results.let {
                it.map { tvDto ->
                    tvDto.toTvEntity(category) }
            }

            CoroutineScope(Dispatchers.Default).launch { movieDatabase.tvDao.upsertTvList(movieEntities) }
            emit(Resource.Success(movieEntities.map { it.toTvShow(category) }))
            emit(Resource.Loading(isLoading = false))
        }
    }

    override suspend fun getAShow(tvShowId: Int): Flow<Resource<TvShow>> {
        return flow {
            // start loading
            emit(Resource.Loading(isLoading = true))

            // get tvShow from cache
            val tvInCache = cacheManager.getFromCache(tvShowId)
            if (tvInCache != null) {
                emit(Resource.Success(tvInCache.tvEntity.toTvShow("")))
                return@flow
            }

            // get tvShow from database
            val tvInDb = movieDatabase.tvDao.getTvById(tvShowId)
            if (tvInDb != null) {

                // cache form database if tvShow is in my list
                if (tvInDb.inMyList) {
                    emit(Resource.Success(tvInDb.toTvShow("")))
                    cacheManager.addToCache(tvShowId, localCache(tvInDb))
                    return@flow
                }

                // cache from remote if tvShow is not in my list
                val tvShowDetailDto = try {
                    tvApi.getATvShow(tvShowId)
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    emit(Resource.Error(message = "Error loading shows"))
                    return@flow
                }
                catch (ex: HttpException) {
                    ex.printStackTrace()
                    emit(Resource.Error(message = "Error loading shows"))
                    Log.d("TestViewModel-TestViewModel", "tvRepo: returning b")
                    return@flow
                }
                catch (ex: Exception) {
                    ex.printStackTrace()
                    emit(Resource.Error(message = "Error loading shows"))
                    return@flow
                }

                emit(Resource.Success(
                    tvInDb.toTvShow("").copy(season_count = tvShowDetailDto.seasons?.size ?: 1)
                ))
                cacheManager.addToCache(tvShowId, remoteCache(tvShowDetailDto))
                return@flow
            }


            // get tvShow from remote
            val tvShowDetailDto = try {
                tvApi.getATvShow(tvShowId)
            } catch (ex: IOException) {
                ex.printStackTrace()
                emit(Resource.Error(message = "Error loading shows"))
                return@flow
            }
            catch (ex: HttpException) {
                ex.printStackTrace()
                emit(Resource.Error(message = "Error loading shows"))
                return@flow
            }
            catch (ex: Exception) {
                ex.printStackTrace()
                emit(Resource.Error(message = "Error loading shows"))
                return@flow
            }

            // emit and cache the tvShow
            emit(Resource.Success(tvShowDetailDto.toTvEntity().toTvShow("")))
            cacheManager.addToCache(tvShowId, remoteCache(tvShowDetailDto))

            // stop loading
            emit(Resource.Loading(isLoading = false))
        }
    }

    override suspend fun getTvEpisodesBySeason(
        tvShowId: Int,
        seasonNumber: Int
    ): Flow<Resource<List<Episode>>> {
        return flow {
            emit(Resource.Loading(isLoading = true))

            val getFromCache = cacheManager.getFromCache(tvShowId)
            if (getFromCache != null) {
                val seasonOrNull = getFromCache.seasons.find { it.seasonNumber == seasonNumber }
                seasonOrNull?.let { season ->
                    emit(Resource.Success(season.episodes.map { it.toEpisode() }))
                }
                emit(Resource.Loading(isLoading = false))
                return@flow
            }

            emit(Resource.Error(message = "Season not available. Try again!"))
            emit(Resource.Loading(isLoading = false))
        }
    }

    override suspend fun getAnEpisode(tvShowId: Int, seasonNumber: Int, episodeId: Int): Flow<Resource<Episode>> {
        return flow {
            emit(Resource.Loading(isLoading = true))

            val episode = cacheManager.getFromCache(tvShowId)
                ?.seasons?.find { it.seasonNumber == seasonNumber }
                ?.episodes?.find { it.id == episodeId }

            if (episode != null) {
                emit(Resource.Success(episode.toEpisode()))
                emit(Resource.Loading(isLoading = false))
                return@flow
            }

            emit(Resource.Error(message = "Episode not available."))
            emit(Resource.Loading(isLoading = false))
        }
    }

    override suspend fun addShowToMyList(tvShowId: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            val showFromCache = cacheManager.getFromCache(tvShowId)
            if (showFromCache != null) {
                showFromCache.seasons.forEach {  season ->
                    movieDatabase.episodeDao.upsertEpisodeList(season.episodes)
                }

                movieDatabase.tvDao.upsertTv(showFromCache.tvEntity.copy(
                    inMyList = true, season_count = showFromCache.numberOfSeason))
            }
        }
    }

    override suspend fun removeShowFromMyList(tvShowId: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            val tvShow = movieDatabase.tvDao.getTvById(tvShowId)
            tvShow?.let{ movieDatabase.tvDao.upsertTv(it.copy(inMyList = false)) }
            movieDatabase.episodeDao.removeEpisodes(tvShowId)
        }
    }

    override suspend fun getShowsInMyList(): Flow<Resource<List<TvShow>>> {
        return flow {
            emit(Resource.Loading(isLoading = true))

            val myList = movieDatabase.tvDao.getAllTvInMyList(true)
            myList.collectLatest { tvList ->
                emit(Resource.Success(tvList.map { it.toTvShow("") }))
                emit(Resource.Loading(isLoading = false))
            }
        }
    }

    private suspend fun remoteCache(tvShowDetailDto: TvShowDetailDto): TvShowModel{
        val mutex = Mutex()
        val seasons = mutableListOf<Season>()

        val jobs = mutableListOf<Job>() // Store references to all launched coroutines

        val getFromDbJob = CoroutineScope(Dispatchers.IO).async {
            movieDatabase.tvDao.getTvById(tvShowDetailDto.id ?: -1)
        }

        repeat(tvShowDetailDto.number_of_seasons ?: 1) { seasonIndex ->
            val job = CoroutineScope(Dispatchers.IO).launch {
                val episodes = tvApi.getATvShowEpisodes(tvShowDetailDto.id ?: -1, seasonIndex + 1)
                val season = Season(
                    showId = tvShowDetailDto.id ?: -1,
                    seasonNumber = seasonIndex + 1,
                    overView = tvShowDetailDto.overview ?: "",
                    numberOfEpisode = episodes.episodes?.size ?: -1,
                    episodes = episodes.episodes?.map { it.toEpisodeEntity() } ?: emptyList()
                )
                mutex.withLock {
                    seasons.add(season)
                }
            }
            jobs.add(job) // Store reference to the launched coroutine
        }

        // Wait for all launched coroutines to complete
        jobs.forEach { it.join() }

        // All coroutines have completed, continue with the rest of your code
        return TvShowModel(id = tvShowDetailDto.id ?: 0,
            tvEntity = getFromDbJob.await() ?: tvShowDetailDto.toTvEntity(),
            numberOfSeason = tvShowDetailDto.number_of_seasons ?: 0, seasons = seasons)
    }

    private suspend fun localCache(tvEntity: TvEntity): TvShowModel {
        val mutex = Mutex()
        val seasons = mutableListOf<Season>()

        val jobs = mutableListOf<Job>() // Store references to all launched coroutines

        repeat(tvEntity.season_count) { seasonIndex ->
            val job = CoroutineScope(Dispatchers.IO).launch {
                val episodes = movieDatabase.episodeDao.getEpisodeByShowAndSeason(tvEntity.id, seasonIndex + 1)
                val season = Season(showId = tvEntity.id, seasonNumber = seasonIndex + 1,
                    numberOfEpisode = episodes.size, overView = tvEntity.overview,
                    episodes = episodes)
                mutex.withLock {
                    seasons.add(season)
                }
            }
            jobs.add(job) // Store reference to the launched coroutine
        }

        // Wait for all launched coroutines to complete
        jobs.forEach { it.join() }

        return TvShowModel(id = tvEntity.id, tvEntity = tvEntity,
            numberOfSeason = tvEntity.season_count, seasons = seasons)
    }

}