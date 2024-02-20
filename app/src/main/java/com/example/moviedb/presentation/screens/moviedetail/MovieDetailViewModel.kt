package com.example.moviedb.presentation.screens.moviedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.domain.usecase.MovieDetailScreenUseCase
import com.example.moviedb.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieDetailScreenUseCase: MovieDetailScreenUseCase,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _movieDetailState = MutableStateFlow(MovieDetailState())
    val movieDetailState = _movieDetailState.asStateFlow()

    init {
        viewModelScope.launch {
            val movieId = savedStateHandle.get<Int>("movie_id")
            movieDetailScreenUseCase.getMovie(movieId ?: -1).collect {
                when (it) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {
                        _movieDetailState.update { state ->
                            state.copy(isLoading = it.isLoading)
                        }
                    }
                    is Resource.Success -> {
                        _movieDetailState.update { state ->
                            state.copy(movie = it.data)
                        }
                    }
                }
            }


            movieId?.let {  id ->
                movieDetailScreenUseCase.getSimilarMoviesLIst(id).collect { result ->
                    when (result) {
                        is Resource.Error -> { }
                        is Resource.Loading -> {
                            _movieDetailState.update { state ->
                                state.copy(isSimilarMoviesLoading = result.isLoading)
                            }
                        }
                        is Resource.Success -> {
                            _movieDetailState.update { state ->
                                state.copy(similarMovies = result.data ?: emptyList())
                            }
                        }
                    }
                }
            }
        }
    }


    fun onEvent(event: MovieDetailUiEvent) {
        when (event) {
            MovieDetailUiEvent.AddOrRemoveFromMyList -> {
                viewModelScope.launch {
                    if (movieDetailState.value.movie?.inMyList != true) {
                        _movieDetailState.update { state ->
                            state.copy(movie = movieDetailState.value.movie?.copy(inMyList = null))
                        }
                        movieDetailState.value.movie?.let { movie ->
                            movieDetailScreenUseCase.addToMyList(movie).also { result ->
                                _movieDetailState.update { state ->
                                    state.copy(movie = movieDetailState.value.movie?.copy(inMyList = result))
                                }
                            }
                        }
                    }else {
                        _movieDetailState.update { state ->
                            state.copy(movie = movieDetailState.value.movie?.copy(inMyList = null))
                        }
                        movieDetailState.value.movie?.let { movie ->
                            movieDetailScreenUseCase.removeFromMyList(movie).also { result ->
                                _movieDetailState.update { state ->
                                    state.copy(movie = movieDetailState.value.movie?.copy(inMyList = result))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}