package com.example.moviedb.data.mapper

import com.example.moviedb.data.local.movie.MovieEntity
import com.example.moviedb.data.local.tv.TvEntity
import com.example.moviedb.data.remote.respond.search.SearchDto
import com.example.moviedb.domain.model.Search

fun SearchDto.toSearch(): Search =
    Search(
        adult = adult ?: false,
        backdrop_path = backdrop_path ?: "",
        first_air_date = first_air_date ?: "",
        genre_ids = genre_ids ?: listOf(),
        id = id ?: -1,
        media_type = media_type ?: "movie",
        name = name ?: "",
        origin_country = origin_country ?: listOf(),
        original_language = original_language ?: "",
        original_name = original_name ?: "",
        original_title = original_title ?: "",
        overview = overview ?: "",
        popularity = popularity ?: 0.0,
        poster_path = poster_path ?: "",
        release_date = release_date ?: "",
        title = title ?: "",
        video = video ?: false,
        vote_average = vote_average ?: 0.0,
        vote_count = vote_count ?: 0

    )


fun MovieEntity.toSearch(): Search =
    Search(
        adult = adult,
        backdrop_path = backdrop_path,
        first_air_date = release_date,
        genre_ids = try {
            genre_ids.split(",").map { it.toInt() }
        }catch (ex: Exception) {
            listOf(-1, -2)
        },
        id = id,
        media_type = "movie",
        name = title,
        origin_country = listOf(),
        original_language = original_language,
        original_name = original_title ,
        original_title = original_title,
        overview = overview,
        popularity = popularity,
        poster_path = poster_path,
        release_date = release_date,
        title = title,
        video = video,
        vote_average = vote_average,
        vote_count = vote_count

    )


fun TvEntity.toSearch(): Search =
    Search(
        adult = adult,
        backdrop_path = poster_path,
        first_air_date = first_air_date,
        genre_ids = try {
            genre_ids.split(",").map { it.toInt() }
        }catch (ex: Exception) {
            listOf(-1, -2)
        },
        id = id,
        media_type = "tv",
        name = name,
        origin_country = try {
            origin_country.split(",").map { it }
        }catch (ex: Exception) {
            listOf("", "")
        },
        original_language = original_language,
        original_name = original_name,
        original_title = original_name,
        overview = overview,
        popularity = popularity,
        poster_path = poster_path,
        release_date = first_air_date,
        title = name,
        video = false,
        vote_average = vote_average,
        vote_count = vote_count

    )