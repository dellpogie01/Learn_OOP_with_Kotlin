package com.juanmediahubph.ooptutorial.ui.components

import android.os.SystemClock
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juanmediahubph.ooptutorial.R
import com.juanmediahubph.ooptutorial.ui.theme.AppTheme
import kotlinx.coroutines.delay

@Composable
internal fun StartupSplash(content: @Composable () -> Unit) {
    var finished by rememberSaveable { mutableStateOf(false) }
    var deadline by rememberSaveable { mutableLongStateOf(0L) }
    if (finished) content() else {
        LaunchedEffect(Unit) {
            withFrameNanos { }
            if (deadline == 0L) deadline = SystemClock.elapsedRealtime() + 3_000L
            delay((deadline - SystemClock.elapsedRealtime()).coerceAtLeast(0L))
            finished = true
        }
        SplashContent()
    }
}

@Composable
private fun SplashContent() {
    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        BoxWithConstraints(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).padding(24.dp)) {
            val logoSize = if (maxHeight < 360.dp) 64.dp else 96.dp
            Column(Modifier.align(Alignment.Center).fillMaxWidth().verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)) {
                Image(painterResource(R.drawable.ic_kotlin_logo), contentDescription = null, modifier = Modifier.size(logoSize))
                Text(stringResource(R.string.home_title), style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground, textAlign = TextAlign.Center)
                Text(stringResource(R.string.splash_subtitle), style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
            }
        }
    }
}

@Preview(name = "Splash light", showBackground = true)
@Preview(name = "Splash dark landscape", widthDp = 640, heightDp = 360, fontScale = 1.3f,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SplashPreview() { AppTheme { SplashContent() } }
