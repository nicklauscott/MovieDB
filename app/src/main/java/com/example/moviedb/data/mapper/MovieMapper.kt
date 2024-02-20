package com.example.moviedb.data.mapper

import com.example.moviedb.data.local.movie.MovieEntity
import com.example.moviedb.data.remote.respond.MovieDto
import com.example.moviedb.data.remote.respond.moviedetaildto.MovieDetailDto
import com.example.moviedb.data.remote.respond.similar.movie.SimilarMovieDto
import com.example.moviedb.domain.model.Movie

fun MovieDto.toMovieEntity(category: String): MovieEntity =
    MovieEntity(
        id = id ?: -1,
        category = category,
        adult = adult ?: false,
        backdrop_path = backdrop_path ?: "",
        original_language = original_language ?: "",
        original_title = original_title ?: "",
        overview = overview ?: "",
        popularity = popularity ?: 0.0,
        poster_path = poster_path ?: "",
        release_date = release_date ?: "",
        title = title ?: "",
        video = video ?: false,
        vote_average = vote_average ?: 0.0,
        vote_count = vote_count ?: 0,
        genre_ids = try {
            genre_ids?.joinToString(",") ?: "-1,-2"
        }catch (ex: Exception) {
            "-1,-2"
        }
    )

fun MovieDetailDto.toMovieEntity(): MovieEntity =
    MovieEntity(
        id = id ?: -1,
        category = "",
        adult = adult ?: false,
        backdrop_path = backdrop_path ?: "",
        genre_ids = genres?.map { it.id.toString() + "," }.toString(),
        original_language = original_language ?: "",
        original_title = original_title ?: "",
        overview = overview ?: "",
        popularity = popularity ?: 0.0,
        poster_path = poster_path ?: "",
        release_date = release_date ?: "",
        title = title ?: "",
        video = video ?: false,
        vote_average = vote_average ?: 0.0,
        vote_count = vote_count ?: 0,
        inMyList = false,

        runtime = runtime ?: -1,
        revenue = revenue ?: -1,
        budget = budget ?: -1,
        status = status ?: "",
        production_companies = production_companies?.map { it.name + "," }.toString(),
        production_countries = production_countries?.map { it.name }.toString()

    )


fun MovieEntity.toMovie(category: String): Movie =
    Movie(
        id = id,
        category = category,
        adult = adult,
        backdrop_path = backdrop_path,
        original_language = original_language,
        original_title = original_title,
        overview = overview,
        popularity = popularity,
        poster_path = poster_path,
        release_date = release_date,
        title = title,
        video = video,
        vote_average = vote_average,
        vote_count = vote_count,
        inMyList = inMyList,

        runtime = runtime,
        revenue = revenue,
        budget = budget,
        status = status,
        production_companies = production_companies,
        production_countries = production_countries,

        genre_ids = try {
            genre_ids.split(",").map { it.toInt() }
        }catch (ex: Exception) {
            listOf(-1, -2)
        }
    )


fun SimilarMovieDto.toMovie(): Movie =
    Movie(
        id = id ?: -1,
        category = "",
        adult = adult ?: false,
        backdrop_path = backdrop_path ?: "",
        genre_ids = genre_ids ?: listOf(),
        original_language = original_language ?: "",
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