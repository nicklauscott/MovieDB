package com.example.moviedb.presentation.screens.moviedetail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.moviedb.R
import com.example.moviedb.domain.model.Constant
import com.example.moviedb.domain.model.Movie
import com.example.moviedb.domain.model.TvShow
import com.example.moviedb.util.RatingBar

@Composable
fun MovieDetailHeader(movie: Movie) {

    val backDropImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(Constant.BASE_IMAGE_URL + movie.backdrop_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    val posterImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(Constant.BASE_IMAGE_URL + movie.poster_path)
            .size(Size.ORIGINAL)
            .build()
    ).state


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {

        // backdrop image
        if (backDropImageState is AsyncImagePainter.State.Error) {
            Icon(
                modifier = Modifier.size(70.dp),
                imageVector = Icons.Rounded.ImageNotSupported,
                contentDescription = movie.title
            )
        }

        if (backDropImageState is AsyncImagePainter.State.Success) {
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                painter = backDropImageState.painter,
                contentDescription = movie.title,
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
                        startY = 100f
                    )
                )
        )

        // poster with detail
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 12.dp, bottom = 10.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth()
            ) {

                // poster
                Card(
                    modifier = Modifier
                        .height(150.dp)
                        .width(100.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (posterImageState is AsyncImagePainter.State.Error) {
                        Icon(
                            modifier = Modifier.size(70.dp),
                            imageVector = Icons.Rounded.ImageNotSupported,
                            contentDescription = movie.title
                        )
                    }

                    if (posterImageState is AsyncImagePainter.State.Success) {
                        Image(
                            modifier = Modifier
                                .fillMaxSize(),
                            painter = posterImageState.painter,
                            contentDescription = movie.title,
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // detail
                Text(
                    text = movie.title,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.SemiBold,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = FontFamily.Serif
                )

                Spacer(modifier = Modifier.height(3.dp))

                Row(
                    modifier = Modifier
                        .padding(start = 0.dp)
                ) {
                    RatingBar(
                        starsModifier = Modifier.size(13.dp),
                        rating = movie.vote_average / 2
                    )

                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = movie.vote_average.toString().take(3),
                        color = Color.LightGray,
                        fontSize = 14.sp,
                        maxLines = 1,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}