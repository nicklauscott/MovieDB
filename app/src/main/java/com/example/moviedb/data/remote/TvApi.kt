package com.example.moviedb.data.remote

import com.example.moviedb.data.remote.respond.tv.TvListDto
import com.example.moviedb.data.remote.respond.tv.episode.EpisodeListDto
import com.example.moviedb.data.remote.respond.tv.tvdetail.TvShowDetailDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvApi {

    @GET("tv/{category}")
    suspend fun getTvShows(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = MovieApi.API_KEY
    ): TvListDto


    @GET("tv/{series_id}")
    suspend fun getATvShow(
        @Path("series_id") seriesId: Int,
        @Query("api_key") apiKey: String = MovieApi.API_KEY
    ): TvShowDetailDto

    @GET("tv/{series_id}/season/{season_number}")
    suspend fun getATvShowEpisodes(
        @Path("series_id") seriesId: Int,
        @Path("season_number") seasonNumber: Int,
        @Query("api_key") apiKey: String = MovieApi.API_KEY
    ): EpisodeListDto

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500"
        const val API_KEY = ""
    }
}