package com.example.moviedb.presentation.screens.tvshowdetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviedb.domain.model.Episode
import com.example.moviedb.domain.model.TvShow

@Composable
fun SeasonToggle(tvShow: TvShow, episodes: List<Episode>, currentSeason: Int, onChangeSeason: (Int) -> Unit) {
    Row(
        modifier = Modifier.padding(16.dp)
            .height(35.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Surface(
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.25f),
            shape = RoundedCornerShape(4.dp)
        ) {
            SeasonSpinner(seasons = (1..tvShow.season_count).toList(),
                currentSeason = currentSeason, onClick = { onChangeSeason(it) })
        }

        Spacer(modifier = Modifier.width(10.dp))

        if (episodes.isNotEmpty()) {
            Row(
                modifier = Modifier.height(35.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 4.dp),
                    text = episodes.size.toString() +
                            if (episodes.size == 1) " episode" else " episodes"
                                    + " " + episodes[0].air_date.split("-")[0],
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
