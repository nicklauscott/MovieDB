package com.example.moviedb.data.remote

import com.example.moviedb.BuildConfig
import com.example.moviedb.data.remote.respond.MovieListDto
import com.example.moviedb.data.remote.respond.similar.movie.SimilarMovieListDto
import com.example.moviedb.data.remote.respond.similar.tvshow.SimilarTvShowListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieApi {

    @GET("movie/{category}")
    suspend fun getMovies(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): MovieListDto

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500"
        const val API_KEY = BuildConfig.API_KEY
    }

    // movie/123/similar?language=en-US&page=1&api_key=231d83b09f2a9487b1139ae666f54e97
    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = MovieApi.API_KEY
    ): SimilarMovieListDto
}