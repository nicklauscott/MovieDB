package com.example.moviedb.data.remote

import com.example.moviedb.data.remote.respond.search.SearchListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchApi {

    @GET("search/{category}")
    suspend fun searchMoviesAndTvShows(
        @Path("category") category: String = "multi",
        @Query("query") searchQuery: String,
        @Query("include_adult") adult: Boolean,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = MovieApi.API_KEY
    ): SearchListDto
}