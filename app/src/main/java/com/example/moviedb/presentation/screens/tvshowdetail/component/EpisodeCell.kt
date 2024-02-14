package com.example.moviedb.presentation.screens.tvshowdetail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.moviedb.domain.model.Constant
import com.example.moviedb.domain.model.Episode

@Composable
fun EpisodeCell(episode: Episode, onClick: (Episode) -> Unit) {

    val showFullOverview = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.05f))
            .clickable { onClick(episode) }
    ) {

        val posterImageState = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(Constant.BASE_IMAGE_URL + episode.still_path)
                .size(Size.ORIGINAL)
                .build()
        ).state

        Row {
            Card(
                modifier = Modifier
                    .height(120.dp)
                    .width(180.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                shape = RoundedCornerShape(topStart = 4.dp, topEnd = 0.dp, bottomStart = 0.dp, bottomEnd = 4.dp)
            ) {
                Box {
                    if (posterImageState is AsyncImagePainter.State.Error) {
                        Icon(
                            modifier = Modifier.size(70.dp),
                            imageVector = Icons.Rounded.ImageNotSupported,
                            contentDescription = episode.name
                        )
                    }

                    if (posterImageState is AsyncImagePainter.State.Success) {
                        Image(
                            modifier = Modifier
                                .fillMaxSize(),
                            painter = posterImageState.painter,
                            contentDescription = episode.name,
                            contentScale = ContentScale.Crop
                        )
                    }

                    // gradient
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black),
                                    startY = 75f
                                )
                            )
                    )

                    // runtime
                    Box(modifier = Modifier.fillMaxSize().padding(5.dp),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Text(
                            text = episode.runtime.toString() + " Min",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 13.sp,
                            maxLines = 1,
                            fontFamily = FontFamily.Serif
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = episode.episode_number.toString() + ". " + episode.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = FontFamily.Serif
                )

                Spacer(modifier = Modifier.height(5.dp))

                Row(horizontalArrangement = Arrangement.Start) {
                    Text(
                        text = "Aired:",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        fontSize = 14.sp,
                        maxLines = 1,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.width(3.dp))


                    Text(
                        text = episode.air_date,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                        fontSize = 13.sp,
                        maxLines = 1,
                        fontFamily = FontFamily.Serif
                    )
                }

            }
        }

        Spacer(modifier = Modifier.height(10.dp))


        Text(
            modifier = Modifier
                .padding(start = 4.dp)
                .clickable {
                    showFullOverview.value = !showFullOverview.value
                },
            text = if (!showFullOverview.value) {
                episode.overview.take(120) + if (episode.overview.length >= 123) "..." else ""
            }
            else episode.overview,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            fontSize = 14.sp,
            fontFamily = FontFamily.Serif
        )

        Spacer(modifier = Modifier.height(5.dp))
        Divider(modifier = Modifier
            .height(0.5.dp)
            .fillMaxWidth(),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
    }
}