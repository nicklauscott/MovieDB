package com.example.moviedb.presentation.screens.homescreen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import com.example.moviedb.presentation.component.CategorySpinner
import com.example.moviedb.presentation.screens.homescreen.HomeScreenState
import com.example.moviedb.presentation.screens.homescreen.HomeScreenUiEvent
import com.example.moviedb.util.Category

@Composable
fun AppTopBar(
    homeScreenState: HomeScreenState,
    currentCategory: State<Category>,
    onChangeCategory: (Category) -> Unit,
    onClickSearch: () -> Unit
) {


    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        color = MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
        tonalElevation = 6.dp,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // category spinner
            CategorySpinner(
                modifier = Modifier,
                items = Category.values(),
                isMovieScreen = homeScreenState.isMovieListScreen,
                currentCategory = currentCategory.value.uiValue
            ) { selectedCategory ->
                onChangeCategory(selectedCategory)
            }

            // search icon
            IconButton(onClick = onClickSearch) {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f))
            }
        }
    }
}