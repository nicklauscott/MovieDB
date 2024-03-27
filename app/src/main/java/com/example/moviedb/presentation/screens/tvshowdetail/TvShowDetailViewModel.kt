package com.example.moviedb.presentation.screens.tvshowdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.domain.usecase.TvDetailScreenUseCase
import com.example.moviedb.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvShowDetailViewModel @Inject constructor(
    private val tvDetailScreenUseCase: TvDetailScreenUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private var _tvShowDetailScreenState = MutableStateFlow(TvShowDetailScreenState())
    val tvShowDetailScreenState = _tvShowDetailScreenState.asStateFlow()

    init {
        viewModelScope.launch {
            val tvShowId = savedStateHandle.get<Int>("show_id")
            launch(Dispatchers.IO) {
                tvDetailScreenUseCase.getTvShow(tvShowId ?: -1) { tvShow ->
                    _tvShowDetailScreenState.update {
                        it.copy(tvShow = tvShow, isEpisodeLoading = false)
                    }
                }
            }.join()

            tvDetailScreenUseCase.getEpisodeLIst(tvShowId ?: -1, 1).collect {
                when (it) {
                    is Resource.Error -> {
                        _tvShowDetailScreenState.update { state ->
                            state.copy(episodeError = it.message)
                        }
                    }
                    is Resource.Loading -> {
                        _tvShowDetailScreenState.update { state ->
                            state.copy(isEpisodeLoading = state.isEpisodeLoading)
                        }
                    }
                    is Resource.Success -> {
                        _tvShowDetailScreenState.update { state ->
                            state.copy(episodes = it.data ?: emptyList())
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: TvShowDetailScreenUiEvent) {
        when (event) {
            TvShowDetailScreenUiEvent.AddOrRemoveFromMyList -> {
                viewModelScope.launch {
                    if (_tvShowDetailScreenState.value.tvShow?.inMyList == true) {
                        _tvShowDetailScreenState.update {
                            it.copy(tvShow = _tvShowDetailScreenState.value.tvShow?.copy(inMyList = null))
                        }
                        tvShowDetailScreenState.value.tvShow?.let {
                            val result = tvDetailScreenUseCase.removeFromMyList(it)  // investigate more
                            _tvShowDetailScreenState.update { state ->
                                state.copy(tvShow = _tvShowDetailScreenState.value.tvShow?.copy(inMyList = !result))
                            }
                        }
                    }else {
                        _tvShowDetailScreenState.update {
                            it.copy(tvShow = _tvShowDetailScreenState.value.tvShow?.copy(inMyList = null))
                        }
                        tvShowDetailScreenState.value.tvShow?.let {
                            val result = tvDetailScreenUseCase.addToMyList(it)
                            _tvShowDetailScreenState.update { state ->
                                state.copy(tvShow = _tvShowDetailScreenState.value.tvShow?.copy(inMyList = result))
                            }
                        }
                    }
                }
            }
            is TvShowDetailScreenUiEvent.SwitchEpisode -> {
                _tvShowDetailScreenState.update {
                    it.copy(isEpisodeLoading = true)
                }
                viewModelScope.launch {
                    _tvShowDetailScreenState.value.tvShow?.let { tvShow ->

                        tvDetailScreenUseCase.getEpisodeLIst(tvShow.id, event.season).collect {
                            when (it) {
                                is Resource.Error -> {
                                    _tvShowDetailScreenState.update { state ->
                                        state.copy(episodeError = it.message)
                                    }
                                }
                                is Resource.Loading -> {
                                    _tvShowDetailScreenState.update { state ->
                                        state.copy(isEpisodeLoading = state.isEpisodeLoading)
                                    }
                                }
                                is Resource.Success -> {
                                    _tvShowDetailScreenState.update { state ->
                                        state.copy(episodes = it.data ?: emptyList())
                                    }
                                }
                            }
                        }
                    }
                }
            }
            TvShowDetailScreenUiEvent.SimilarShows -> {
                viewModelScope.launch {
                    if (tvShowDetailScreenState.value.similarTvShows.isEmpty()) {
                        tvDetailScreenUseCase.getSimilarTvShowLIst(tvShowDetailScreenState.value.tvShow?.id ?: -1,
                            tvShowDetailScreenState.value.tvShow?.genre_ids ?: emptyList()
                        )
                            .collect {
                                when (it) {
                                    is Resource.Error -> {}
                                    is Resource.Loading -> {
                                        _tvShowDetailScreenState.update { state ->
                                            state.copy(isSimilarTvShowsLoading = it.isLoading)
                                        }
                                    }
                                    is Resource.Success -> {
                                        _tvShowDetailScreenState.update { state ->
                                            state.copy(similarTvShows = it.data ?: emptyList())
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

