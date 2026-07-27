package com.pricecheck.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Tokens lifted straight from the mockup's tailwind.config
private val md_primary = Color(0xFF00685F)
private val md_onPrimary = Color(0xFFFFFFFF)
private val md_primaryContainer = Color(0xFF008378)
private val md_onPrimaryContainer = Color(0xFFF4FFFC)
private val md_secondary = Color(0xFF006B5F)
private val md_onSecondary = Color(0xFFFFFFFF)
private val md_secondaryContainer = Color(0xFF62FAE3)
private val md_onSecondaryContainer = Color(0xFF007165)
private val md_tertiary = Color(0xFF006B2D)
private val md_onTertiary = Color(0xFFFFFFFF)
private val md_tertiaryContainer = Color(0xFF00873B)
private val md_onTertiaryContainer = Color(0xFFF7FFF3)
private val md_error = Color(0xFFBA1A1A)
private val md_onError = Color(0xFFFFFFFF)
private val md_errorContainer = Color(0xFFFFDAD6)
private val md_onErrorContainer = Color(0xFF93000A)
private val md_background = Color(0xFFF8F9FA)
private val md_onBackground = Color(0xFF191C1D)
private val md_surface = Color(0xFFF8F9FA)
private val md_onSurface = Color(0xFF191C1D)
private val md_surfaceVariant = Color(0xFFE1E3E4)
private val md_onSurfaceVariant = Color(0xFF3D4947)
private val md_outline = Color(0xFF6D7A77)
private val md_outlineVariant = Color(0xFFBCC9C6)
private val md_surfaceContainerLowest = Color(0xFFFFFFFF)
private val md_surfaceContainerLow = Color(0xFFF3F4F5)
private val md_surfaceContainer = Color(0xFFEDEEEF)
private val md_surfaceContainerHigh = Color(0xFFE7E8E9)
private val md_surfaceContainerHighest = Color(0xFFE1E3E4)

private val AppColorScheme = lightColorScheme(
    primary = md_primary,
    onPrimary = md_onPrimary,
    primaryContainer = md_primaryContainer,
    onPrimaryContainer = md_onPrimaryContainer,
    secondary = md_secondary,
    onSecondary = md_onSecondary,
    secondaryContainer = md_secondaryContainer,
    onSecondaryContainer = md_onSecondaryContainer,
    tertiary = md_tertiary,
    onTertiary = md_onTertiary,
    tertiaryContainer = md_tertiaryContainer,
    onTertiaryContainer = md_onTertiaryContainer,
    error = md_error,
    onError = md_onError,
    errorContainer = md_errorContainer,
    onErrorContainer = md_onErrorContainer,
    background = md_background,
    onBackground = md_onBackground,
    surface = md_surface,
    onSurface = md_onSurface,
    surfaceVariant = md_surfaceVariant,
    onSurfaceVariant = md_onSurfaceVariant,
    outline = md_outline,
    outlineVariant = md_outlineVariant,
    surfaceContainerLowest = md_surfaceContainerLowest,
    surfaceContainerLow = md_surfaceContainerLow,
    surfaceContainer = md_surfaceContainer,
    surfaceContainerHigh = md_surfaceContainerHigh,
    surfaceContainerHighest = md_surfaceContainerHighest,
)

@Composable
fun PriceCheckTheme(content: @Composable () -> Unit) {
    // ponytail: dark theme not in scope, mockup only specifies light tokens
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography(),
        content = content
    )
}
