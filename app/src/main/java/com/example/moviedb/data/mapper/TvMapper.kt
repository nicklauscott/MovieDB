package com.example.moviedb.data.mapper

import com.example.moviedb.data.local.tv.TvEntity
import com.example.moviedb.data.remote.respond.similar.tvshow.SimilarTvShowDto
import com.example.moviedb.data.remote.respond.tv.TvDto
import com.example.moviedb.data.remote.respond.tv.tvdetail.TvShowDetailDto
import com.example.moviedb.domain.model.TvShow

fun TvDto.toTvEntity(category: String): TvEntity =
    TvEntity(
        id = id ?: -1,
        adult = adult ?: false,
        backdrop_path = backdrop_path ?: "",
        first_air_date = first_air_date ?: "",
        media_type = media_type ?: "",
        name = name ?: "",
        original_language = original_language ?: "",
        original_name = original_name ?: "",
        overview = overview ?: "",
        popularity = popularity ?: 0.0,
        poster_path = poster_path ?: "",
        vote_average = vote_average ?: 0.0,
        vote_count = vote_count ?: 0,
        inMyList = false,
        category = category,

        origin_country = try {
            origin_country?.joinToString(",") ?: "xx,xx"
        }catch (ex: Exception) {
            "xx,xx"
        },

        genre_ids = try {
            genre_ids?.joinToString(",") ?: "-1,-2"
        }catch (ex: Exception) {
            "-1,-2"
        }
    )

fun TvEntity.toTvShow(category: String): TvShow =
    TvShow(
        id = id,
        adult = adult,
        backdrop_path = backdrop_path,
        first_air_date = first_air_date,
        media_type = media_type,
        name = name,
        original_language = original_language,
        original_name = original_name,
        overview = overview,
        popularity = popularity,
        season_count = season_count,
        poster_path = poster_path,
        vote_average = vote_average,
        vote_count = vote_count,
        inMyList = inMyList,
        category = category,

        origin_country = try {
            origin_country.split(",").map { it }
        }catch (ex: Exception) {
            listOf("xx", "xx")
        },
        genre_ids = try {
            genre_ids.split(",").map { it.toInt() }
        }catch (ex: Exception) {
            listOf(-1, -2)
        }
    )

fun TvShowDetailDto.toTvEntity(): TvEntity =
    TvEntity(
        id = id ?: 0,
        media_type = "",
        backdrop_path = backdrop_path ?: "",
        name = name ?: "",
        original_name = original_name ?: "",
        genre_ids = "",
        season_count = seasons?.size ?: 0,
        adult = adult ?: false,
        first_air_date = first_air_date ?: "",
        origin_country =  try {
            origin_country?.joinToString(",") ?: "xx,xx"
        }catch (ex: Exception) {
            "xx,xx"
        },
        original_language = original_language ?: "",
        overview = overview ?: "",
        popularity = popularity ?: 0.0,
        poster_path = poster_path ?: "",
        vote_average = vote_average ?: 0.0,
        vote_count = vote_count ?: 0,
        category = "",
        inMyList = false
    )

fun SimilarTvShowDto.toTvShow(): TvShow =
    TvShow(
        id = id ?: -1,
        adult = adult ?: false,
        backdrop_path = backdrop_path ?: "",
        first_air_date = first_air_date ?: "",
        genre_ids = genre_ids ?: listOf(),
        season_count = 1,
        media_type = "tv",
        name = name ?: "",
        origin_country = origin_country ?: listOf(),
        original_language = original_language ?: "",
        original_name = original_name ?: "",
        overview = overview ?: "",
        popularity = popularity ?: 0.0,
        poster_path = poster_path ?: "",
        vote_average = vote_average ?: 0.0,
        vote_count = vote_count ?: 0,
        inMyList = null,
        category = ""
    )