package com.example.moviedb.presentation.screens.homescreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.moviedb.R
import com.example.moviedb.presentation.navigation.Screens
import com.example.moviedb.presentation.screens.homescreen.HomeScreenUiEvent

data class BottomItem(
    val title: String, val icon: ImageVector
)

@Composable
fun BottomNavigationBar(
    bottomNavController: NavHostController,
    isMovieScreen: Boolean,
    onEvent: () -> Unit
) {

    val items = listOf(
        BottomItem(
            title = stringResource(R.string.movies),
            icon = Icons.Rounded.Movie
        ), BottomItem(
            title = stringResource(R.string.tvShows),
            icon = Icons.Rounded.Upcoming
        )
    )

    val selected = rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar {
        Row(
            modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            items.forEachIndexed { index, bottomItem ->
                NavigationBarItem(selected = selected.intValue == index, onClick = {
                    selected.intValue = index
                    when (selected.intValue) {
                        0 -> {
                            if (!isMovieScreen) {
                                onEvent()
                                bottomNavController.popBackStack()
                                bottomNavController.navigate(Screens.MovieList.route)
                            }
                        }

                        1 -> {
                            if (isMovieScreen) {
                                onEvent()
                                bottomNavController.popBackStack()
                                bottomNavController.navigate(Screens.TvShowLIst.route)
                            }
                        }
                    }
                }, icon = {
                    Icon(
                        imageVector = bottomItem.icon,
                        contentDescription = bottomItem.title,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }, label = {
                    Text(
                        text = bottomItem.title, color = MaterialTheme.colorScheme.onBackground
                    )
                })
            }
        }
    }

}