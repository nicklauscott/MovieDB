package com.example.moviedb.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moviedb.util.Category


@Composable
fun CategorySpinner(items: Array<Category>,
                    currentCategory: String, onClick: (Category) -> Unit) {
    val expanded = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.width(120.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp))
            .clickable { expanded.value = true }
            .padding(start = 5.dp, end = 5.dp)
        ) {
            Column(modifier = Modifier
                .weight(0.65f)
                .fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center) {
                Text(text = currentCategory, fontSize = 18.sp,
                    style = MaterialTheme.typography.bodyMedium)
            }
            Column(modifier = Modifier
                .weight(0.35f)
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
        items.forEach { item ->
            DropdownMenuItem(text = { Text(text = item.uiValue,
                style = MaterialTheme.typography.headlineSmall) },
                onClick = {
                    expanded.value = false
                    onClick(item)
                })
        }
    }
}