package com.example.moviedb.data.remote.respond.search

data class SearchListDto(
    val page: Int,
    val results: List<SearchDto>,
    val total_pages: Int,
    val total_results: Int
)