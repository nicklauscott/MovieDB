package com.example.moviedb.presentation.screens.tvshowdetail.component

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
import com.example.moviedb.domain.model.TvShow
import com.example.moviedb.presentation.component.MatureRating
import com.example.moviedb.util.countryCodesToNames

@Composable
fun TvShowDetailBody(tvShow: TvShow, isEpisodeVisible: Boolean,
                     onClickEpisodeOrRelated: (Int) -> Unit,
                     onclickAdd: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ){
        val showFullOverview = remember {
            mutableStateOf(false)
        }

        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            val year = tvShow.first_air_date.split("-")[0]
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = year,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 14.sp,
                maxLines = 1,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.width(8.dp))

            MatureRating(adult = tvShow.adult)

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = tvShow.season_count.toString() + if (tvShow.season_count == 1) " Season" else " Seasons",
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
                tvShow.overview.take(145) + if (tvShow.overview.length >= 150) "..." else ""
            }
            else tvShow.overview,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 14.sp,
            fontFamily = FontFamily.Serif
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (tvShow.name != tvShow.original_name) {
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
                    text = tvShow.original_name,
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
                text = "Original Country:",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                fontSize = 14.sp,
                maxLines = 1,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.width(3.dp))
            val countries = tvShow.origin_country.map {
                countryCodesToNames[it]
            }

            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = countries.joinToString(", "),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp,
                maxLines = 1,
                fontFamily = FontFamily.Serif
            )
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

                if (tvShow.inMyList == null) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(15.dp),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
                }else {
                    Icon(imageVector =
                    if (tvShow.inMyList == true) Icons.Rounded.Check else Icons.Rounded.Add,
                        contentDescription =
                        if (tvShow.inMyList == true) "Remove from my list" else "Add to my list"
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

        Divider(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
        Row(

        ) {
            // episode
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .clickable(enabled = !isEpisodeVisible) {
                        onClickEpisodeOrRelated(1)
                    }
            ) {
                Divider(modifier = Modifier
                    .height(3.dp)
                    .width(70.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 0.dp, topEnd = 0.dp, bottomStart = 4.dp, bottomEnd = 4.dp
                        )
                    ),
                    color = if (isEpisodeVisible) MaterialTheme.colorScheme.primary else Color.Transparent
                    )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(start = 4.dp),
                    text = "Episodes",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(5.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            // more like this
            Column(
                modifier = Modifier.clickable(enabled = isEpisodeVisible) {
                    onClickEpisodeOrRelated(2)
                }
            ) {
                Divider(modifier = Modifier
                    .height(3.dp)
                    .width(110.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 0.dp, topEnd = 0.dp, bottomStart = 4.dp, bottomEnd = 4.dp
                        )
                    ),
                    color = if (!isEpisodeVisible) MaterialTheme.colorScheme.primary else Color.Transparent
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


