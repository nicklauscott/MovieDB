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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
                }.shuffled()
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

            val tvShowEntities = movieLIstFromRemote.results.let {
                it.map { tvDto ->
                    tvDto.toTvEntity(category) }
            }

            CoroutineScope(Dispatchers.Default).launch { movieDatabase.tvDao.upsertTvList(tvShowEntities) }
            emit(Resource.Success(tvShowEntities.map { it.toTvShow(category) }.shuffled()))
            emit(Resource.Loading(isLoading = false))
        }
    }

    override suspend fun getAShow(tvShowId: Int): Resource<TvShow>{
        // get tvShow from cache
        val tvInCache = cacheManager.getFromCache(tvShowId)
        if (tvInCache != null) {
            return (Resource.Success(tvInCache.tvEntity.toTvShow("")))
        }

        // get tvShow from database
        val tvInDb = movieDatabase.tvDao.getTvById(tvShowId)
        if (tvInDb != null) {

            // cache form database if tvShow is in my list
            if (tvInDb.inMyList) {
                cacheManager.addToCache(tvShowId, localCache(tvInDb))  // ---------------------------------------------------------------------------
                return Resource.Success(tvInDb.toTvShow(""))
            }

            // cache from remote if tvShow is not in my list
            val tvShowDetailDto = try {
                tvApi.getATvShow(tvShowId)
            } catch (ex: IOException) {
                ex.printStackTrace()
                return Resource.Error(message = "Error loading shows")
            }
            catch (ex: HttpException) {
                ex.printStackTrace()
                return Resource.Error(message = "Error loading shows")
            }
            catch (ex: Exception) {
                ex.printStackTrace()
                return Resource.Error(message = "Error loading shows")
            }

            cacheManager.addToCache(tvShowId, remoteCache(tvShowDetailDto))
            return  Resource.Success(
                tvInDb.toTvShow("").copy(season_count = tvShowDetailDto.seasons?.size ?: 1)
            )
        }

        // get tvShow from remote
        val tvShowDetailDto = try {
            tvApi.getATvShow(tvShowId)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return Resource.Error(message = "Error loading shows")
        }
        catch (ex: HttpException) {
            ex.printStackTrace()
            return Resource.Error(message = "Error loading shows")
        }
        catch (ex: Exception) {
            ex.printStackTrace()
            return Resource.Error(message = "Error loading shows")
        }

        // emit and cache the tvShow
        cacheManager.addToCache(tvShowId, remoteCache(tvShowDetailDto))
        return Resource.Success(tvShowDetailDto.toTvEntity().toTvShow(""))
    }

     override suspend fun getTvEpisodesBySeason(tvShowId: Int, seasonNumber: Int): Flow<Resource<List<Episode>>>{
        return flow {
            cacheManager.getFlowFromCache(tvShowId).collect {
                when (it) {
                    is Resource.Error -> {
                        emit(Resource.Error(it.message))
                        return@collect
                    }
                    is Resource.Success -> {
                        it.data?.seasons?.find { seasons -> seasons.seasonNumber == seasonNumber }?.let {  episodes ->
                            emit(Resource.Success(episodes.episodes.map { episodeEntity ->  episodeEntity.toEpisode() }))
                        }
                        emit(Resource.Loading(false))
                        return@collect
                    }
                    else -> {

                    }
                }
            }
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

    override suspend fun addShowToMyList(tvShowId: Int): Boolean{
        CoroutineScope(Dispatchers.Default).launch {
            val showFromCache = cacheManager.getFromCache(tvShowId)
            if (showFromCache != null) {
                showFromCache.seasons.forEach {  season ->
                    movieDatabase.episodeDao.upsertEpisodeList(season.episodes)
                }
                val updatedTvShow = showFromCache.tvEntity.copy(
                    inMyList = true, season_count = showFromCache.numberOfSeason)

                cacheManager.addToCache(tvShowId, localCache(updatedTvShow))
                movieDatabase.tvDao.insertTv(updatedTvShow)
            }
        }.join()
        return true
    }

    override suspend fun removeShowFromMyList(tvShowId: Int): Boolean{
        CoroutineScope(Dispatchers.Default).launch {
            val tvShow = movieDatabase.tvDao.getTvById(tvShowId)
            tvShow?.let{
                cacheManager.addToCache(tvShowId, localCache(it.copy(inMyList = false)))
                movieDatabase.tvDao.insertTv(it.copy(inMyList = false))
            }
            movieDatabase.episodeDao.removeEpisodes(tvShowId)
        }.join()
        return true
    }

    override suspend fun getShowsInMyList(): Flow<Resource<List<TvShow>>> {
        return flow {
            emit(Resource.Loading(isLoading = true))

            movieDatabase.tvDao.getAllTvInMyList(true).collect { tvList ->
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
            tvEntity = tvShowDetailDto.toTvEntity().copy(
                season_count = tvShowDetailDto.number_of_seasons ?: 0, inMyList = getFromDbJob.await()?.inMyList ?: false
            ),
            numberOfSeason = tvShowDetailDto.number_of_seasons ?: 0, seasons = seasons)
    }

    private suspend fun localCache(tvEntity: TvEntity): TvShowModel {
        val mutex = Mutex()
        val seasons = mutableListOf<Season>()

        val jobs = mutableListOf<Job>() // Store references to all launched coroutines

        repeat(tvEntity.season_count) { seasonIndex ->
            val job = CoroutineScope(Dispatchers.Default).launch {
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

    override suspend fun getSimilarTvShows(tvShowId: Int, genres: List<Int>): Flow<Resource<List<TvShow>>> {
        return flow {
            emit(Resource.Loading(isLoading = true))

            val similarTvShowListDto = try {
                tvApi.getSimilarTvShows(tvShowId, 1)
            } catch (ex: IOException) {
                ex.printStackTrace()
                emit(Resource.Success(getOffLineSimilarShows(tvShowId, genres)))
                emit(Resource.Loading(isLoading = false))
                return@flow
            }
            catch (ex: HttpException) {
                ex.printStackTrace()
                emit(Resource.Error(message = "Error loading shows"))
                emit(Resource.Loading(isLoading = false))
                return@flow
            }
            catch (ex: Exception) {
                ex.printStackTrace()
                emit(Resource.Error(message = "Error loading shows"))
                emit(Resource.Loading(isLoading = false))
                return@flow
            }

            emit(Resource.Success(similarTvShowListDto.results.map { it.toTvShow() }))
            emit(Resource.Loading(isLoading = false))
        }
    }

    private suspend fun getOffLineSimilarShows(tvShowId: Int, genre: List<Int>): List<TvShow> {
        return CoroutineScope(Dispatchers.IO).async {
            val shows = movieDatabase.tvDao.getAllShow().map { it.toTvShow("") }
            shows.filter { show ->
                genre.any { genreId ->
                    show.genre_ids.contains(genreId)
                }
            }.filter { it.id != tvShowId }
        }.await()
    }
}
