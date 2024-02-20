package com.example.moviedb.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviedb.domain.usecase.SearchListUsecase
import com.example.moviedb.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchListUsecase: SearchListUsecase
): ViewModel(){

    private var _searchScreenState = MutableStateFlow(SearchScreenState())
    val searchScreenState = _searchScreenState.asStateFlow()

    fun onEvent(event: SearchUiEvent) {
        when (event) {
            SearchUiEvent.Paginate -> {
                if (!searchScreenState.value.localSearch && searchScreenState.value.searchList.size >= 20) {
                    _searchScreenState.update {
                        it.copy(searchPage = searchScreenState.value.searchPage + 1)
                    }
                    viewModelScope.launch {
                        searchListUsecase(
                            path = searchScreenState.value.type.pathName,
                            localSearch = false,
                            searchQuery = searchScreenState.value.searchQuery,
                            adult = !searchScreenState.value.safeSearch,
                            page = searchScreenState.value.searchPage

                        ).collect {
                            when (it) {
                                is Resource.Error -> {
                                    _searchScreenState.update { state ->
                                        state.copy(message = it.message, isLoading = false)
                                    }
                                }
                                is Resource.Loading -> {
                                    _searchScreenState.update { state ->
                                        state.copy(isLoading = it.isLoading)
                                    }
                                }
                                is Resource.Success -> {
                                    _searchScreenState.update { state ->
                                        state.copy(searchList = searchScreenState.value.searchList + (it.data ?: emptyList()))
                                    }
                                }
                            }
                        }
                    }
                }
            }
            SearchUiEvent.ToggleSafeSearch -> {
                _searchScreenState.update {
                    it.copy(safeSearch = !searchScreenState.value.safeSearch)
                }
            }
            SearchUiEvent.ToggleLocalSearch -> {
                _searchScreenState.update {
                    it.copy(localSearch = !searchScreenState.value.localSearch)
                }
            }
            is SearchUiEvent.ChangeType -> {
                _searchScreenState.update {
                    it.copy(type = event.type)
                }
            }
            is SearchUiEvent.Search -> {
                _searchScreenState.update {
                    it.copy(searchQuery = event.searchQuery)
                }
                viewModelScope.launch {
                    searchListUsecase(
                        path = searchScreenState.value.type.pathName,
                        localSearch = searchScreenState.value.localSearch,
                        searchQuery = event.searchQuery,
                        adult = !searchScreenState.value.safeSearch,
                        page = searchScreenState.value.searchPage

                    ).collect {
                        when (it) {
                            is Resource.Error -> {
                                _searchScreenState.update { state ->
                                    state.copy(message = it.message, isLoading = false)
                                }
                            }
                            is Resource.Loading -> {
                                _searchScreenState.update { state ->
                                    state.copy(isLoading = it.isLoading)
                                }
                            }
                            is Resource.Success -> {
                                _searchScreenState.update { state ->
                                    state.copy(
                                        searchList = it.data ?: emptyList(),
                                        message = if (it.data.isNullOrEmpty()) "No movies matching the search query" else null
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}