package com.example.moviedb.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MatureRating(adult: Boolean) {
    Surface(
        color = Color.LightGray.copy(alpha = 0.5f),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            modifier = Modifier
                .padding(start = 4.dp),
            text = if (adult) "18+ " else "13+ ",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 13.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}