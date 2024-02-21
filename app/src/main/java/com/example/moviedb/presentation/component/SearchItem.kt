package com.example.moviedb.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.moviedb.data.remote.MovieApi
import com.example.moviedb.domain.model.Search

@Composable
fun SearchItem(
    search: Search,
    onSearchClick: (Search) -> Unit
) {
    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MovieApi.BASE_IMAGE_URL + search.backdrop_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable { onSearchClick(search) }
    ) {

        Box(modifier = Modifier
            .width(180.dp)
            .padding(6.dp)
            .height(100.dp)
            .clip(RoundedCornerShape(6.dp))) {

            if (imageState is AsyncImagePainter.State.Error) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(70.dp),
                        imageVector = Icons.Rounded.ImageNotSupported,
                        contentDescription = search.title
                    )
                }
            }

            if (imageState is AsyncImagePainter.State.Success) {
                Image(
                    painter = imageState.painter,
                    contentDescription = search.title,
                    contentScale = ContentScale.Crop
                )
            }

            if (search.media_type == "tv") {
                Surface(
                    shape = RoundedCornerShape(bottomStart = 2.dp, bottomEnd = 8.dp),
                    color = Color.Red.copy(0.8f),
                    shadowElevation = 6.dp
                ) {
                    Text(
                        text = "TV",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        fontSize = 15.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = FontFamily.Serif,
                        modifier = Modifier.padding(3.dp)
                    )
                }
            }

        }


        Spacer(modifier = Modifier.width(10.dp))

        Column {

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = search.name,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 15.sp,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily.Serif
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = if (search.media_type == "tv") search.first_air_date.split("-")[0]
                else search.release_date.split("-")[0],
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 13.sp,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily.Serif
            )
        }

        // gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = 10f
                    )
                )
        )
    }

}