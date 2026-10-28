package com.juanmediahubph.ooptutorial.ui.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import com.juanmediahubph.ooptutorial.ui.theme.singleLineScale

@Composable
internal fun FittingSingleLineText(
    text: String,
    style: TextStyle,
    color: Color,
    modifier: Modifier = Modifier
) {
    val measurer = rememberTextMeasurer()
    BoxWithConstraints(modifier.fillMaxWidth()) {
        val availableWidth = constraints.maxWidth
        val fitted = remember(text, style, availableWidth, measurer) {
            var lower = 0.1f
            var upper = 1f
            repeat(12) {
                val scale = (lower + upper) / 2f
                val width = measurer.measure(text, style.singleLineScale(scale), softWrap = false, maxLines = 1).size.width
                if (width <= availableWidth - 2) lower = scale else upper = scale
            }
            style.singleLineScale(lower)
        }
        Text(text, style = fitted, color = color, maxLines = 1, softWrap = false)
    }
}
