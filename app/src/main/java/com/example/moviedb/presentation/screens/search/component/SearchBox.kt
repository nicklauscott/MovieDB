package com.example.moviedb.presentation.screens.search.component

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchBox(searchQuery: String,
              onQueryChange: (String) -> Unit,
              onQueryClear: () -> Unit,
              onSearch: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(MaterialTheme.colorScheme.onBackground.copy(0.2f)),
        contentAlignment = Alignment.Center
    ) {


        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 55.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (searchQuery.isBlank()) {
                Text(
                    text = "Search movies or shows",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.headlineMedium,
                    fontFamily = FontFamily.Serif
                )
            }
        }

        // clear search text field
        OutlinedTextField(
            modifier = Modifier
                .fillMaxSize(),
            value = searchQuery, onValueChange = { newQuery ->
                onQueryChange(newQuery)
            },
            textStyle = TextStyle(
                fontSize = 15.sp,
                fontFamily = FontFamily.Serif
            ),
            leadingIcon = {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    modifier = Modifier.size(30.dp))
                Spacer(modifier = Modifier.width(5.dp))
            },
            trailingIcon = {
                Spacer(modifier = Modifier.width(5.dp))

                // clear searchQuery
                if (searchQuery.isNotBlank()) {
                    Column(
                        modifier = Modifier
                            .padding(5.dp)
                            .clip(CircleShape)
                            .clickable { onQueryClear() }
                            .padding(5.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            modifier = Modifier
                                .size(30.dp))
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                cursorColor = MaterialTheme.colorScheme.onBackground.copy(0.8f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
                focusedContainerColor = Color.Transparent
            ),
            shape = RectangleShape,
            singleLine = true,
            label = {},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch()
                }
            )
        )

    }
}