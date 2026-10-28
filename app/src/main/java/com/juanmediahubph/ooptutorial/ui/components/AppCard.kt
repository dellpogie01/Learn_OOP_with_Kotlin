package com.juanmediahubph.ooptutorial.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun AppCard(
    modifier: Modifier = Modifier,
    colors: CardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(modifier = modifier, shape = MaterialTheme.shapes.large, colors = colors,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp), content = content)
}

@Composable
internal fun AppCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: CardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(onClick = onClick, modifier = modifier, shape = MaterialTheme.shapes.large, colors = colors,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp, pressedElevation = 2.dp), content = content)
}
