package com.example.moviedb.di

import com.example.moviedb.data.repository.MovieRepositoryImpl
import com.example.moviedb.data.repository.SearchRepositoryImpl
import com.example.moviedb.data.repository.TvShowRepositoryImp
import com.example.moviedb.domain.repository.MovieRepository
import com.example.moviedb.domain.repository.SearchRepository
import com.example.moviedb.domain.repository.TvShowRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository

    @Binds
    @Singleton
    abstract fun bindTvRepository(
        tvShowRepositoryImp: TvShowRepositoryImp
    ): TvShowRepository


    @Binds
    @Singleton
    abstract fun bindSearchRepository(
        searchRepositoryImp: SearchRepositoryImpl
    ): SearchRepository
}