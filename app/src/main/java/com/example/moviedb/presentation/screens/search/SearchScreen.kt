package com.example.moviedb.presentation.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moviedb.presentation.component.SearchItem
import com.example.moviedb.presentation.navigation.Screens
import com.example.moviedb.presentation.screens.search.component.FilterSection
import com.example.moviedb.presentation.screens.search.component.SearchBox

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(navController: NavController = rememberNavController(),
                 viewModel: SearchViewModel = hiltViewModel()) {

    val state = viewModel.searchScreenState.collectAsState()

    val searchQuery = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val showSearchFilter = remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                color = MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                tonalElevation = 6.dp,
                contentColor = MaterialTheme.colorScheme.onBackground
            ) {
                Row(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    // search icon
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f))
                    }
                }
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            SearchBox(
                searchQuery = searchQuery.value,
                onQueryChange = { newQuery ->
                    searchQuery.value = newQuery
                    if (searchQuery.value.isNotBlank()) {
                        viewModel.onEvent(SearchUiEvent.Search(searchQuery.value))
                    }
                },
                onQueryClear = { searchQuery.value = "" }) {
                keyboardController?.hide()
                if (searchQuery.value.isNotBlank()) {
                    viewModel.onEvent(SearchUiEvent.Search(searchQuery.value))
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
            ) {
                item {
                    // filter
                    FilterSection(
                        showOption = showSearchFilter.value,
                        isLocal = state.value.localSearch,
                        onLocalChange = {
                            viewModel.onEvent(SearchUiEvent.ToggleLocalSearch)
                            if (searchQuery.value.isNotBlank()) {
                                viewModel.onEvent(SearchUiEvent.Search(searchQuery.value))
                            }
                        },
                        isAdult = state.value.safeSearch,
                        onAdultChange = {
                            viewModel.onEvent(SearchUiEvent.ToggleSafeSearch)
                            if (searchQuery.value.isNotBlank()) {
                                viewModel.onEvent(SearchUiEvent.Search(searchQuery.value))
                            }
                        },
                        onTypeChange = { type ->
                            viewModel.onEvent(SearchUiEvent.ChangeType(type))
                            if (searchQuery.value.isNotBlank()) {
                                viewModel.onEvent(SearchUiEvent.Search(searchQuery.value))
                            }
                        }) {
                        showSearchFilter.value = !showSearchFilter.value
                    }
                }

                // search items
                when  {
                    state.value.isLoading -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .height(500.dp)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    else -> {
                        if (state.value.searchList.isEmpty() && state.value.message != null) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .height(500.dp)
                                        .fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = state.value.message ?: "Oops! Couldn't find any movies with such name",
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                        fontSize = 15.sp,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontFamily = FontFamily.Serif
                                    )
                                }
                            }
                        } else {
                            items(state.value.searchList.size) { index ->
                                SearchItem(search = state.value.searchList[index]) {searchItem ->
                                    if (state.value.type == Type.Both && searchItem.media_type == "tv") {
                                        navController.navigate(Screens.TvShowDetail.withArg(searchItem.id.toString()))
                                        return@SearchItem
                                    }
                                    if (state.value.type == Type.TvShows) {
                                        navController.navigate(Screens.TvShowDetail.withArg(searchItem.id.toString()))
                                        return@SearchItem
                                    }
                                    navController.navigate(Screens.MovieDetail.withArg(searchItem.id.toString()))
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                if (index >= state.value.searchList.size - 1 && !state.value.isLoading) {
                                    viewModel.onEvent(SearchUiEvent.Paginate)
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}



