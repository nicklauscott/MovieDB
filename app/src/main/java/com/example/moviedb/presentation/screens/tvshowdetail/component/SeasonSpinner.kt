package com.example.moviedb.presentation.screens.tvshowdetail.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviedb.util.Category
import com.example.moviedb.util.Type


@Composable
fun SeasonSpinner(
    modifier: Modifier = Modifier, seasons: List<Int>,
                    currentSeason: Int, onClick: (Int) -> Unit) {
    val expanded = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier.wrapContentWidth() // width(120.dp)
    ) {
        Row(modifier = Modifier
            .wrapContentWidth()
            .fillMaxHeight()
            .clip(RoundedCornerShape(8.dp))
            .clickable { expanded.value = true }
            .padding(start = 5.dp, end = 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center) {
                Text(text = "Season $currentSeason", fontSize = 18.sp,
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily.Monospace)
                Spacer(modifier = Modifier.width(5.dp))
            }
            Column(modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center) {
                Icon(imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Show more")
            }
        }

    }
    DropdownMenu(expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        modifier = Modifier.wrapContentSize()) {
        // subjects
        seasons.forEach { season ->
            DropdownMenuItem(text = { Text(text = "Season $season",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily.Monospace) },
                onClick = {
                    expanded.value = false
                    onClick(season)
                })
        }
    }
}