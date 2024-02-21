package com.example.moviedb.presentation.screens.moviedetail.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviedb.domain.model.Movie
import com.example.moviedb.domain.model.TvShow
import com.example.moviedb.presentation.component.MatureRating
import com.example.moviedb.util.countryCodesToNames

@Composable
fun MovieDetailBody(movie: Movie, onclickAdd: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val showFullOverview = remember {
            mutableStateOf(false)
        }

        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            val year = movie.release_date.split("-")[0]
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = year,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 14.sp,
                maxLines = 1,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.width(8.dp))

            MatureRating(adult = movie.adult)

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = movie.status,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 14.sp,
                maxLines = 1,
                fontFamily = FontFamily.Monospace
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        Text(
            modifier = Modifier
                .padding(start = 4.dp)
                .clickable(enabled = !showFullOverview.value) {
                    showFullOverview.value = true
                },
            text = if (!showFullOverview.value) {
                movie.overview.take(220) + if (movie.overview.length >= 225) "..." else ""
            } else movie.overview,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 14.sp,
            fontFamily = FontFamily.Serif
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (movie.title != movie.original_title) {
            Row(horizontalArrangement = Arrangement.Start) {
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = "Original Name:",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    fontSize = 14.sp,
                    maxLines = 1,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = movie.original_title,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    maxLines = 1,
                    fontFamily = FontFamily.Serif
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))


        Row(horizontalArrangement = Arrangement.Start) {
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = "Released Date:",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                fontSize = 14.sp,
                maxLines = 1,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = movie.release_date,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp,
                maxLines = 1,
                fontFamily = FontFamily.Serif
            )
        }


        Spacer(modifier = Modifier.height(5.dp))

        if (movie.runtime != -1) {
            Row(horizontalArrangement = Arrangement.Start) {
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = "Runtime:",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    fontSize = 14.sp,
                    maxLines = 1,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = movie.runtime.toString() + " Min",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    maxLines = 1,
                    fontFamily = FontFamily.Serif
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))


        Row(horizontalArrangement = Arrangement.Start) {
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = "Original Language:",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                fontSize = 14.sp,
                maxLines = 1,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = movie.original_language,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp,
                maxLines = 1,
                fontFamily = FontFamily.Serif
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        if (movie.budget != -1) {
            Row(horizontalArrangement = Arrangement.Start) {
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = "Production Budget:",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    fontSize = 14.sp,
                    maxLines = 1,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = "$" + movie.budget.toString(),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    maxLines = 1,
                    fontFamily = FontFamily.Serif
                )
            }
        }


        Spacer(modifier = Modifier.height(5.dp))

        if (movie.revenue != -1) {
            Row(horizontalArrangement = Arrangement.Start) {
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = "Revenue:",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    fontSize = 14.sp,
                    maxLines = 1,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = "$" + movie.revenue.toString(),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    maxLines = 1,
                    fontFamily = FontFamily.Serif
                )
            }
        }





        Spacer(modifier = Modifier.height(5.dp))

        Row(horizontalArrangement = Arrangement.Start) {
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = "Vote Count:",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                fontSize = 14.sp,
                maxLines = 1,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = movie.vote_count.toString(),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp,
                maxLines = 1,
                fontFamily = FontFamily.Serif
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        val productionCountries = movie.production_countries
            .substring(startIndex = 1, endIndex = movie.production_countries.length - 1)
        if (productionCountries.isNotBlank()) {
            Row(horizontalArrangement = Arrangement.Start) {
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = "Production Countries:",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    fontSize = 14.sp,
                    maxLines = 1,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = productionCountries,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Serif
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        val productionCompanies = movie.production_companies
            .substring(startIndex = 1, endIndex = movie.production_companies.length - 1)
        if (productionCompanies.isNotBlank()) {
            Row(horizontalArrangement = Arrangement.Start) {
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = "Production Companies:",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    fontSize = 14.sp,
                    maxLines = 1,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = productionCompanies,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Serif
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            // add or remove from my list
            Column(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(80.dp)
                    .clickable { onclickAdd() },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (movie.inMyList == null) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(15.dp),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                } else {
                    Icon(
                        imageVector =
                        if (movie.inMyList == true) Icons.Rounded.Check else Icons.Rounded.Add,
                        contentDescription =
                        if (movie.inMyList == true) "Remove from my list" else "Add to my list"
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    modifier = Modifier
                        .padding(start = 4.dp),
                    text = "My List",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
        )
        Row(

        ) {
            // more like this
            Column {
                Divider(
                    modifier = Modifier
                        .height(3.dp)
                        .width(110.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 0.dp, topEnd = 0.dp, bottomStart = 4.dp, bottomEnd = 4.dp
                            )
                        ),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier
                        .padding(start = 4.dp),
                    text = "More Like This",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Serif
                )

                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}


