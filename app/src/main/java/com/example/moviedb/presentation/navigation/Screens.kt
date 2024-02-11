package com.example.moviedb.presentation.navigation

enum class Screens(val route: String) {
    SPlash("splash_screen"),
    Home("home_screen"),
    Search("search_screen"),
    MovieList("movie_list_screen"),
    TvShowLIst("tv_show_list_screen"),
    MovieDetail("movie_detail_screen"),
    TvShowDetail("tv_show_detail_screen");

    fun withArg(vararg args: String): String{
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}


