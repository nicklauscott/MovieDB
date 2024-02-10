package com.example.moviedb.data.remote.respond.tv

data class TvListDto(
    val page: Int,
    val results: List<TvDto>,
    val total_pages: Int,
    val total_results: Int
)