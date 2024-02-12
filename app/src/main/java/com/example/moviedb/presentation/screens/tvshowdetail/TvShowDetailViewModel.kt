package com.example.moviedb.presentation.screens.tvshowdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.domain.usecase.TvDetailScreenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
            tvDetailScreenUseCase.getTvShowDetail(tvShowId ?: -1) { tvShow, episodes ->
                _tvShowDetailScreenState.update {
                    it.copy(tvShow = tvShow, episodes = episodes ?: emptyList())
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
                            val result = tvDetailScreenUseCase.removeFromMyList(it)
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
                        val newEpisodes = tvDetailScreenUseCase.getEpisodeLIst(tvShow.id, event.season)
                        _tvShowDetailScreenState.update {
                            it.copy(episodes = newEpisodes ?: emptyList(), isEpisodeLoading = false)
                        }
                    }
                }
            }
            TvShowDetailScreenUiEvent.RelatedShows -> TODO()
        }
    }
}

