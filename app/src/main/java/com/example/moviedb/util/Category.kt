package com.example.moviedb.util

enum class Category(val value: String, val uiValue: String) {
    MyList("my_list", "My List"),
    Popular("popular", "Popular"),
    Upcoming("upcoming", "Upcoming"),
    Trending("trending", "Trending")
}