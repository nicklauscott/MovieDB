package com.example.moviedb.data.remote

import com.example.moviedb.data.remote.respond.search.SearchListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    // https://api.themoviedb.org/3/search/multi?query=love&include_adult=false&language=en-US&page=1&api_key=231d83b09f2a9487b1139ae666f54e97

    @GET("search/multi")
    suspend fun searchMoviesAndTvShows(
        @Query("query") searchQuery: String,
        @Query("include_adult") adult: Boolean,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = MovieApi.API_KEY
    ): SearchListDto
}