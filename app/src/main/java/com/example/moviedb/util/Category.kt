package com.example.moviedb.util

enum class Category(val value: String, val uiValue: String, val type: Type) {
    Popular("popular", "Popular", Type.Both),
    NowPlaying("now_playing", "Now Playing", Type.Movie),
    Upcoming("upcoming", "Upcoming", Type.Movie),
    AiringToday("airing_today", "Airing Today", Type.TvShow),
    OnTheAir("on_the_air", "On TheAir", Type.TvShow),
    TopRated("top_rated", "Top Rated", Type.Both),
    MyList("my_list", "My List", Type.Both)
}

enum class Type {
    Movie,
    TvShow,
    Both
}