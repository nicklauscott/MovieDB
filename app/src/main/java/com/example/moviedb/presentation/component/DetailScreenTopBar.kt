package com.example.moviedb.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
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
fun DetailScreenTopBar(
    onClickBack: () -> Unit,
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
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // search icon
            IconButton(onClick = onClickBack) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f))
            }

            // search icon
            IconButton(onClick = onClickSearch) {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f))
            }
        }
    }
}