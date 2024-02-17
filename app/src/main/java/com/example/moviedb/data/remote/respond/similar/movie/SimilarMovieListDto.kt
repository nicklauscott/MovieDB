package com.example.moviedb.data.remote.respond.similar.movie

data class SimilarMovieListDto(
    val page: Int,
    val results: List<SimilarMovieDto>,
    val total_pages: Int,
    val total_results: Int
)