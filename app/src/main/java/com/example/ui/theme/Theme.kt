package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val GramKartLightColorScheme = lightColorScheme(
    primary = GramGreenPrimary,
    onPrimary = GramWhite,
    primaryContainer = GramGreenContainer,
    onPrimaryContainer = GramGreenDark,
    secondary = GramOrangeAccent,
    onSecondary = GramWhite,
    secondaryContainer = GramOrangeContainer,
    onSecondaryContainer = GramOrangeDark,
    tertiary = GramGreenLight,
    onTertiary = GramWhite,
    background = GramBackground,
    onBackground = GramTextPrimary,
    surface = GramWhite,
    onSurface = GramTextPrimary,
    surfaceVariant = GramSurfaceVariant,
    onSurfaceVariant = GramTextSecondary,
    outline = GramBorder,
    outlineVariant = GramBorder,
    error = GramError,
    onError = GramWhite,
    errorContainer = GramErrorContainer,
    onErrorContainer = GramError
)

val GramKartShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun GramKartTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GramKartLightColorScheme,
        typography = GramKartTypography,
        shapes = GramKartShapes,
        content = content
    )
}
