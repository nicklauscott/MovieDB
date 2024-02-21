package com.example.moviedb.di

import android.content.Context
  import androidx.room.Room
import com.example.moviedb.data.local.MovieDatabase
import com.example.moviedb.data.remote.MovieApi
import com.example.moviedb.data.remote.TvApi
import com.example.moviedb.data.cache.CacheManger
import com.example.moviedb.data.remote.SearchApi
import com.example.moviedb.domain.repository.MovieRepository
import com.example.moviedb.domain.repository.SearchRepository
import com.example.moviedb.domain.repository.TvShowRepository
import com.example.moviedb.domain.usecase.AddToMyList
import com.example.moviedb.domain.usecase.GetEpisodeLIst
import com.example.moviedb.domain.usecase.GetMovie
import com.example.moviedb.domain.usecase.GetMovieList
import com.example.moviedb.domain.usecase.GetSimilarMoviesLIst
import com.example.moviedb.domain.usecase.GetSimilarTvShowLIst
import com.example.moviedb.domain.usecase.GetTvShow
import com.example.moviedb.domain.usecase.GetTvShowList
import com.example.moviedb.domain.usecase.HomeScreenUseCase
import com.example.moviedb.domain.usecase.MovieDetailScreenUseCase
import com.example.moviedb.domain.usecase.RemoveFromMyList
import com.example.moviedb.domain.usecase.SearchListUsecase
import com.example.moviedb.domain.usecase.TvDetailScreenUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.HEADERS
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Singleton
    @Provides
    fun provideMoviesApi(): MovieApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(MovieApi.BASE_URL)
            .client(client)
            .build()
            .create(MovieApi::class.java)
    }

    @Singleton
    @Provides
    fun provideTvApi(): TvApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(MovieApi.BASE_URL)
            .client(client)
            .build()
            .create(TvApi::class.java)
    }

    @Singleton
    @Provides
    fun provideSearchApi(): SearchApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(MovieApi.BASE_URL)
            .client(client)
            .build()
            .create(SearchApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "movie.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideCacheManger(): CacheManger {
        return CacheManger()
    }

    // use cases
    @Singleton
    @Provides
    fun provideGetMovieListUsecase(movieRepository: MovieRepository): GetMovieList{
        return GetMovieList(movieRepository)
    }

    @Singleton
    @Provides
    fun provideGetMTvShowListUsecase(tvShowRepository: TvShowRepository): GetTvShowList{
        return GetTvShowList(tvShowRepository)
    }

    @Singleton
    @Provides
    fun provideGetTvShowDetailUsecase(tvShowRepository: TvShowRepository): GetTvShow{
        return GetTvShow(tvShowRepository)
    }

    @Singleton
    @Provides
    fun provideAddToMyListUsecase(movieRepository: MovieRepository,
        tvShowRepository: TvShowRepository): AddToMyList{
        return AddToMyList(movieRepository, tvShowRepository)
    }

    @Singleton
    @Provides
    fun provideRemoveFromMyListUsecase(movieRepository: MovieRepository,
                                  tvShowRepository: TvShowRepository): RemoveFromMyList{
        return RemoveFromMyList(movieRepository, tvShowRepository)
    }

    @Singleton
    @Provides
    fun provideGetEpisodeListUsecase(tvShowRepository: TvShowRepository): GetEpisodeLIst{
        return GetEpisodeLIst(tvShowRepository)
    }


    // SimilarTvShowLIst
    @Singleton
    @Provides
    fun provideGetSimilarTvShowLIst(tvShowRepository: TvShowRepository): GetSimilarTvShowLIst {
        return GetSimilarTvShowLIst(tvShowRepository)
    }

    @Singleton
    @Provides
    fun provideSearchListUsecase(searchRepository: SearchRepository): SearchListUsecase{
        return SearchListUsecase(searchRepository)
    }


    @Singleton
    @Provides
    fun provideGetMovieUsecase(movieRepository: MovieRepository): GetMovie{
        return GetMovie(movieRepository)
    }

    @Singleton
    @Provides
    fun provideGetSimilarMoviesUsecase(movieRepository: MovieRepository): GetSimilarMoviesLIst{
        return GetSimilarMoviesLIst(movieRepository)
    }

    @Singleton
    @Provides
    fun provideHomeScreenUsecase(
        getMovieList: GetMovieList, getTvShowList: GetTvShowList
    ): HomeScreenUseCase = HomeScreenUseCase(getMovieList, getTvShowList)


    @Singleton
    @Provides
    fun provideTvDetailScreenUsecase(
        getTvShow: GetTvShow,
        getEpisodeLIst: GetEpisodeLIst,
        addToMyList: AddToMyList,
        removeFromMyList: RemoveFromMyList,
        getSimilarTvShowLIst: GetSimilarTvShowLIst
    ): TvDetailScreenUseCase =
        TvDetailScreenUseCase(getTvShow, getEpisodeLIst, addToMyList, removeFromMyList, getSimilarTvShowLIst)



    @Singleton
    @Provides
    fun provideMovieDetailUseCase(
        getMovie: GetMovie,
        getSimilarMoviesLIst: GetSimilarMoviesLIst,
        addToMyList: AddToMyList,
        removeFromMyList: RemoveFromMyList
    ): MovieDetailScreenUseCase =
        MovieDetailScreenUseCase(getMovie, getSimilarMoviesLIst, addToMyList, removeFromMyList)
}






