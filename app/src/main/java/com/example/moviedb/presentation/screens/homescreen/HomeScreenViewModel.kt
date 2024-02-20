package com.example.moviedb.presentation.screens.homescreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.domain.usecase.HomeScreenUseCase
import com.example.moviedb.util.Category
import com.example.moviedb.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val homeScreenUseCase: HomeScreenUseCase
) : ViewModel() {

    private var _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState = _homeScreenState.asStateFlow()

    init {
        viewModelScope.launch {
            homeScreenUseCase.getMovieList(false, homeScreenState.value.category, 1) { result ->
                result?.let { movieList ->
                    _homeScreenState.update {
                        it.copy(
                            movieList = homeScreenState.value.movieList + movieList.shuffled(),
                            movieListPage = 1,
                        )
                    }
                }
            }

            homeScreenUseCase.getTvShowList(false, homeScreenState.value.category, 1) { result ->
                result?.let { tvList ->
                    _homeScreenState.update {
                        it.copy(tvShowList = tvList, tvListPage = 1)
                    }
                }
            }
        }
    }

    fun onEvent(event: HomeScreenUiEvent) {
        when (event) {
            HomeScreenUiEvent.Navigate -> {
                _homeScreenState.update {
                    it.copy(
                        isMovieListScreen = !homeScreenState.value.isMovieListScreen,
                        category = Category.Popular
                    )
                }

                // load selected category
                if (homeScreenState.value.isMovieListScreen) {
                    _homeScreenState.update {
                        it.copy(
                            movieList = emptyList(),
                            movieListPage = 1,
                        )
                    }
                    viewModelScope.launch {
                        homeScreenUseCase.getMovieList(true, homeScreenState.value.category, 1) { result ->
                            result?.let { movieList ->
                                _homeScreenState.update {
                                    it.copy(
                                        movieList = homeScreenState.value.movieList + movieList.shuffled(),
                                        movieListPage = 1,
                                    )
                                }
                            }
                        }
                    }
                } else {
                    _homeScreenState.update {
                        it.copy(tvShowList = emptyList(), tvListPage = 1)
                    }
                    viewModelScope.launch {
                        homeScreenUseCase.getTvShowList(true, homeScreenState.value.category, 1) { result ->
                            result?.let { tvList ->
                                _homeScreenState.update {
                                    it.copy(tvShowList = tvList, tvListPage = 1)
                                }
                            }
                        }
                    }
                }
            }
            is HomeScreenUiEvent.Paginate -> {
                when (_homeScreenState.value.isMovieListScreen) {
                    true ->  {
                        if (homeScreenState.value.category != Category.MyList) {
                            //getMovies(homeScreenState.value.movieListPage + 1)
                            viewModelScope.launch {
                                homeScreenUseCase.getMovieList(
                                    true, homeScreenState.value.category,
                                    homeScreenState.value.movieListPage + 1) { result ->
                                    result?.let { movieList ->
                                        _homeScreenState.update {
                                            it.copy(
                                                movieList = homeScreenState.value.movieList + movieList,
                                                movieListPage = homeScreenState.value.movieListPage + 1,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    false -> {
                        if (homeScreenState.value.category != Category.MyList) {
                            //getTvShows(homeScreenState.value.movieListPage + 1)
                            viewModelScope.launch {
                                homeScreenUseCase.getTvShowList(
                                    true, homeScreenState.value.category,
                                    homeScreenState.value.tvListPage + 1) { result ->
                                    result?.let { tvList ->
                                        _homeScreenState.update {
                                            it.copy(tvShowList = homeScreenState.value.tvShowList + tvList,
                                                tvListPage = homeScreenState.value.tvListPage + 1)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            is HomeScreenUiEvent.SwitchCategory -> {
                when (_homeScreenState.value.isMovieListScreen) {
                    true ->  {
                        _homeScreenState.update { it.copy(category = event.category, movieList = emptyList()) }
                        viewModelScope.launch {
                            homeScreenUseCase.getMovieList(
                                false, event.category,
                                1) { result ->
                                result?.let { movieList ->
                                    _homeScreenState.update {
                                        it.copy(
                                            movieList = if (event.category == Category.MyList) movieList
                                                    else homeScreenState.value.movieList + movieList,
                                            movieListPage = homeScreenState.value.movieListPage + 1,
                                        )
                                    }
                                }
                            }
                        }
                    }
                    false -> {
                        _homeScreenState.update { it.copy(category = event.category, tvShowList = emptyList()) }
                        viewModelScope.launch {
                            homeScreenUseCase.getTvShowList(
                                false, event.category, 1) { result ->
                                result?.let { tvList ->
                                    _homeScreenState.update {
                                        it.copy(tvShowList = if (event.category == Category.MyList) tvList
                                            else homeScreenState.value.tvShowList + tvList.shuffled(),
                                            tvListPage = 1)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}