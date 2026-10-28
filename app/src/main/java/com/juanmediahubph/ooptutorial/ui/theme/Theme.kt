package com.juanmediahubph.ooptutorial.ui.theme

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView

private fun Context.activity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.activity()
    else -> null
}

@Composable
fun AppTheme(dark: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        DisposableEffect(view, dark) {
            view.context.activity()?.let { activity ->
                val bars = if (dark) SystemBarStyle.dark(TransparentSystemBar)
                    else SystemBarStyle.light(TransparentSystemBar, TransparentSystemBar)
                activity.enableEdgeToEdge(statusBarStyle = bars, navigationBarStyle = bars)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    activity.window.isNavigationBarContrastEnforced = false
                }
            }
            onDispose { }
        }
    }
    // Keep a consistent indigo identity on all Android versions.
    MaterialTheme(
        colorScheme = if (dark) DarkColors else LightColors,
        typography = AppTypography, shapes = AppShapes, content = content
    )
}

// Preserve the existing public entry point for callers outside this file.
@Composable
fun OopTutorialTheme(dark: Boolean, content: @Composable () -> Unit) {
    AppTheme(dark = dark, content = content)
}
