package com.app.sudokuapp.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkPalette = darkColorScheme(
    primary = SoftPrimary,
    onPrimary = SoftOnPrimary,
    primaryContainer = SoftPrimaryContainer,
    onPrimaryContainer = SoftOnPrimaryContainer,
    secondary = GentleSecondary,
    onSecondary = GentleOnSecondary,
    secondaryContainer = GentleSecondaryContainer,
    onSecondaryContainer = GentleOnSecondaryContainer,
    tertiary = LightTertiary,
    onTertiary = LightOnTertiary,
    tertiaryContainer = LightTertiaryContainer,
    onTertiaryContainer = LightOnTertiaryContainer,
    error = MildError,
    onError = MildOnError,
    errorContainer = MildErrorContainer,
    onErrorContainer = MildOnErrorContainer,
    background = PaleBackground,
    onBackground = PaleOnBackground,
    surface = PaleSurface,
    onSurface = PaleOnSurface,
    outline = FineOutline,
    surfaceVariant = SurfaceVariantTone,
    onSurfaceVariant = OnSurfaceVariantTone
)

private val LightPalette = lightColorScheme(
    primary = SoftPrimary,
    onPrimary = SoftOnPrimary,
    primaryContainer = SoftPrimaryContainer,
    onPrimaryContainer = SoftOnPrimaryContainer,
    secondary = GentleSecondary,
    onSecondary = GentleOnSecondary,
    secondaryContainer = GentleSecondaryContainer,
    onSecondaryContainer = GentleOnSecondaryContainer,
    tertiary = LightTertiary,
    onTertiary = LightOnTertiary,
    tertiaryContainer = LightTertiaryContainer,
    onTertiaryContainer = LightOnTertiaryContainer,
    error = MildError,
    onError = MildOnError,
    errorContainer = MildErrorContainer,
    onErrorContainer = MildOnErrorContainer,
    background = PaleBackground,
    onBackground = PaleOnBackground,
    surface = PaleSurface,
    onSurface = PaleOnSurface,
    outline = FineOutline,
    surfaceVariant = SurfaceVariantTone,
    onSurfaceVariant = OnSurfaceVariantTone
)

@Composable
fun SudokuAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkPalette
    } else {
        LightPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        content = content
    )
}