package com.example.moviedb.presentation.screens.search.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FilterSection(showOption: Boolean,
                  isLocal: Boolean, onLocalChange: () -> Unit,
                  isAdult: Boolean, onAdultChange: () -> Unit,
                  onFilterChange: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Column(
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onFilterChange() }
                .padding(5.dp)
        ) {
            Icon(imageVector = Icons.Default.ManageSearch, contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier
                    .size(30.dp))
        }

        Spacer(modifier = Modifier.height(5.dp))
        AnimatedVisibility(visible = showOption) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Local search",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.headlineMedium,
                        fontFamily = FontFamily.Serif
                    )

                    Switch(checked = isLocal, onCheckedChange = { onLocalChange() },
                        colors = SwitchDefaults.colors(
                            checkedIconColor = MaterialTheme.colorScheme.primary,
                            uncheckedIconColor = MaterialTheme.colorScheme.onBackground
                        ))
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Safe search",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.headlineMedium,
                        fontFamily = FontFamily.Serif
                    )

                    Switch(checked = isAdult, onCheckedChange = { onAdultChange() },
                        colors = SwitchDefaults.colors(
                            checkedIconColor = MaterialTheme.colorScheme.primary,
                            uncheckedIconColor = MaterialTheme.colorScheme.onBackground
                        ))
                }
            }
        }
    }
}
