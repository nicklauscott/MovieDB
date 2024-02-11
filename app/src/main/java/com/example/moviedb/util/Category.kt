package com.example.moviedb.util

enum class Category(val value: String, val uiValue: String) {
    Popular("popular", "Popular"),
    Trending("trending", "Trending"),
    Upcoming("upcoming", "Upcoming"),
    MyList("my_list", "My List")
}