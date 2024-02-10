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
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val homeScreenUseCase: HomeScreenUseCase
) : ViewModel() {

    private var _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState = _homeScreenState.asStateFlow()

    //private var loadMoviesAndShowJOb: Job

    init {
//        loadMoviesAndShowJOb = viewModelScope.launch {
//            val job1 = launch { getMovies() }
//            job1.join()
//            getTvShows()
//        }

        getMovies()
        getTvShows()
    }

    fun onEvent(event: HomeScreenUiEvent) {
        when (event) {
            HomeScreenUiEvent.Navigate -> {
                //loadMoviesAndShowJOb.cancel()
                _homeScreenState.update {
                    it.copy(
                        isMovieListScreen = !homeScreenState.value.isMovieListScreen
                    )
                }
            }
            is HomeScreenUiEvent.Paginate -> {
                when (_homeScreenState.value.isMovieListScreen) {
                    true ->  {
                        if (homeScreenState.value.category != Category.MyList) {
                            getMovies(homeScreenState.value.movieListPage + 1)
                        }
                    }
                    false -> {
                        if (homeScreenState.value.category != Category.MyList) {
                            getTvShows(homeScreenState.value.movieListPage + 1)
                        }
                    }
                }
            }
            is HomeScreenUiEvent.SwitchCategory -> {
                when (_homeScreenState.value.isMovieListScreen) {
                    true ->  {
                        _homeScreenState.update { it.copy(category = event.category) }
                        switchMovieCategory(event.category)
                    }
                    false -> {
                        _homeScreenState.update { it.copy(category = event.category) }
                        switchTvShowCategory(event.category)
                    }
                }
            }
            is HomeScreenUiEvent.Search -> {
                // TODO
            }
        }
    }

    private fun switchMovieCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            homeScreenUseCase.getMovieList(
                forceFetchFromRemote = true,
                category = category,
                page = 1
            ).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        withContext(Dispatchers.Main) {
                            _homeScreenState.update { it.copy(isLoading = false) }
                        }
                    }
                    is Resource.Loading -> {
                        withContext(Dispatchers.Main) {
                            _homeScreenState.update { it.copy(isLoading = result.isLoading) }
                        }
                    }
                    is Resource.Success -> {
                        withContext(Dispatchers.Main) {
                            result.data?.let { movieList ->
                                _homeScreenState.update {
                                    it.copy(
                                        movieList = homeScreenState.value.movieList + movieList.shuffled(),
                                        //isLoading = false
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun switchTvShowCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            homeScreenUseCase.getTvShowList(
                forceFetchFromRemote = true,
                category = category,
                page = 1
            ).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        withContext(Dispatchers.Main) {
                            _homeScreenState.update { it.copy(isLoading = false) }
                        }
                    }
                    is Resource.Loading -> {
                        withContext(Dispatchers.Main) {
                            _homeScreenState.update { it.copy(isLoading = result.isLoading) }
                        }
                    }
                    is Resource.Success -> {
                        withContext(Dispatchers.Main) {
                            result.data?.let { tvList ->
                                _homeScreenState.update {
                                    it.copy(
                                        tvShowList = if (category ==  Category.MyList) tvList else
                                            homeScreenState.value.tvShowList + tvList.shuffled(),
                                        //isLoading = false
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getMovies(page: Int = 1) {
        viewModelScope.launch {
            homeScreenUseCase.getMovieList(
                forceFetchFromRemote = true,
                category = homeScreenState.value.category,
                page = page
            ).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        withContext(Dispatchers.Main) {
                            result.data?.let { movieList ->
                                _homeScreenState.update {
                                    it.copy(
                                        movieList = homeScreenState.value.movieList + movieList.shuffled(),
                                        movieListPage = page,
                                        //isLoading = false
                                    )
                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        withContext(Dispatchers.Main) {
                            _homeScreenState.update { it.copy(isLoading = false) }
                        }
                    }
                    is Resource.Loading -> {
                        withContext(Dispatchers.Main) {
                            _homeScreenState.update { it.copy(isLoading = result.isLoading) }
                        }
                    }
                }
            }
        }
    }

    private fun getTvShows(page: Int = 1) {
        viewModelScope.launch {
            homeScreenUseCase.getTvShowList(
                forceFetchFromRemote = true,
                category = homeScreenState.value.category,
                page = page
            ).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        withContext(Dispatchers.Main) {
                            result.data?.let { tvList ->
                                _homeScreenState.update {
                                    it.copy(
                                        tvShowList = homeScreenState.value.tvShowList + tvList.shuffled(),
                                        //tvListPage = page
                                    )
                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        // let get movies handle loading states at the beginning
                        if (page != 1) {
                            withContext(Dispatchers.Main) {
                                _homeScreenState.update { it.copy(isLoading = false) }
                            }
                        }
                    }
                    is Resource.Loading -> {
                        // let get movies handle loading states at the beginning
                        if (page != 1) {
                            withContext(Dispatchers.Main) {
                                _homeScreenState.update { it.copy(isLoading = result.isLoading) }
                            }
                        }
                    }
                }
            }
        }
    }

}