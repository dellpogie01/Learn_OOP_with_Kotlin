package com.juanmediahubph.ooptutorial.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

// Indigo brand seed: #4F46E5. Teal and mauve accents share neutral violet surfaces.
internal val TransparentSystemBar = Color.Transparent.toArgb()

internal val LightColors = lightColorScheme(
    primary = Color(0xFF4F46E5), onPrimary = Color.White,
    primaryContainer = Color(0xFFE5E1FF), onPrimaryContainer = Color(0xFF211A65),
    secondary = Color(0xFF006874), onSecondary = Color.White,
    secondaryContainer = Color(0xFFB0ECF3), onSecondaryContainer = Color(0xFF002F35),
    tertiary = Color(0xFF76546D), onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFD7EF), onTertiaryContainer = Color(0xFF2D1227),
    background = Color(0xFFF8F8FF), onBackground = Color(0xFF1B1B23),
    inverseSurface = Color(0xFF303038), inverseOnSurface = Color(0xFFF3EFF9), inversePrimary = Color(0xFFC6BFFF),
    surfaceTint = Color(0xFF4F46E5), surfaceBright = Color(0xFFFCF8FF), surfaceDim = Color(0xFFDBD8E2),
    scrim = Color.Black,
    surface = Color(0xFFFCF8FF), onSurface = Color(0xFF1B1B23),
    surfaceVariant = Color(0xFFE5E1EF), onSurfaceVariant = Color(0xFF464451),
    surfaceContainerLowest = Color.White, surfaceContainerLow = Color(0xFFF5F2FC),
    surfaceContainer = Color(0xFFEFECF6), surfaceContainerHigh = Color(0xFFEAE7F0),
    surfaceContainerHighest = Color(0xFFE4E1EB),
    outline = Color(0xFF777481), outlineVariant = Color(0xFFC8C4D2),
    error = Color(0xFFBA1A1A), onError = Color.White,
    errorContainer = Color(0xFFFFDAD6), onErrorContainer = Color(0xFF410002)
)
internal val DarkColors = darkColorScheme(
    primary = Color(0xFFC6BFFF), onPrimary = Color(0xFF282078),
    primaryContainer = Color(0xFF403793), onPrimaryContainer = Color(0xFFE5E1FF),
    secondary = Color(0xFF80D5DF), onSecondary = Color(0xFF00363D),
    secondaryContainer = Color(0xFF004F58), onSecondaryContainer = Color(0xFFB0ECF3),
    tertiary = Color(0xFFE5BAD5), onTertiary = Color(0xFF43273D),
    tertiaryContainer = Color(0xFF5C3D54), onTertiaryContainer = Color(0xFFFFD7EF),
    background = Color(0xFF121318), onBackground = Color(0xFFE5E1EB),
    inverseSurface = Color(0xFFE5E1EB), inverseOnSurface = Color(0xFF303038), inversePrimary = Color(0xFF4F46E5),
    surfaceTint = Color(0xFFC6BFFF), surfaceBright = Color(0xFF38383F), surfaceDim = Color(0xFF121318),
    scrim = Color.Black,
    surface = Color(0xFF121318), onSurface = Color(0xFFE5E1EB),
    surfaceVariant = Color(0xFF464451), onSurfaceVariant = Color(0xFFC8C4D2),
    surfaceContainerLowest = Color(0xFF0D0E13), surfaceContainerLow = Color(0xFF1B1C22),
    surfaceContainer = Color(0xFF1F2026), surfaceContainerHigh = Color(0xFF292A30),
    surfaceContainerHighest = Color(0xFF34343B),
    outline = Color(0xFF928E9C), outlineVariant = Color(0xFF464451),
    error = Color(0xFFFFB4AB), onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A), onErrorContainer = Color(0xFFFFDAD6)
)

