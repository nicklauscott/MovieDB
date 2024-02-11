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

    private var job: Job? = null

    init {
        getMovies()
        getTvShows()
    }

    fun onEvent(event: HomeScreenUiEvent) {
        when (event) {
            HomeScreenUiEvent.Navigate -> {
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
                        job?.cancel()
                        _homeScreenState.update { it.copy(category = event.category, tvShowList = emptyList()) }
                        switchTvShowCategory(event.category)
                    }
                }
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
        job = viewModelScope.launch {
            homeScreenUseCase.getTvShowList(
                forceFetchFromRemote = true,
                category = category,
                page = 1
            ).collectLatest { result ->
                Log.d("TestViewModel-TestViewModel", "result: ${result.data}")
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
                                        tvShowList = if (category != Category.MyList) homeScreenState.value.tvShowList + tvList.shuffled()
                                            else tvList
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