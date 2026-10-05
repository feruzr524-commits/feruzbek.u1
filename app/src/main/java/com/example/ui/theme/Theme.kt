package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GamingDarkColorScheme = darkColorScheme(
    primary = NeonYellow,
    onPrimary = ObsidianBlack,
    primaryContainer = CyberSurfaceVariant,
    onPrimaryContainer = NeonYellowBright,
    secondary = ElectricCyan,
    onSecondary = ObsidianBlack,
    secondaryContainer = DeepSpaceBlue,
    onSecondaryContainer = ElectricCyan,
    tertiary = TacticalAmber,
    onTertiary = ObsidianBlack,
    background = ObsidianBlack,
    onBackground = TextWhite,
    surface = CyberSurface,
    onSurface = TextWhite,
    surfaceVariant = CyberSurfaceVariant,
    onSurfaceVariant = TextMutedCyan,
    error = PrankMagenta,
    onError = Color.White,
    outline = ElectricCyan
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GamingDarkColorScheme,
        typography = Typography,
        content = content
    )
}
