package com.example.moviedb.data.remote.respond.similar.tvshow

data class SimilarTvShowListDto(
    val page: Int,
    val results: List<SimilarTvShowDto>,
    val total_pages: Int,
    val total_results: Int
)