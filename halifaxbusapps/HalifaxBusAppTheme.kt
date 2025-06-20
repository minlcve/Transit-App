package com.example.halifaxbusapps.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Soft pink for light theme
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFAE6E7),
    onPrimary = Color(0xFF6B3F3F),
    secondary = Color(0xFFC49DA5),
    onSecondary = Color.White,
    background = Color(0xFFFFFBFC),
    onBackground = Color(0xFF3A2A2A),
    surface = Color(0xFFFFF5F6),
    onSurface = Color(0xFF3A2A2A),
    error = Color(0xFFB00020),
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF9CCAFF),
    onPrimary = Color.Black,
    secondary = Color(0xFFBCC6D2),
    background = Color(0xFF1B1B1F),
    surface = Color(0xFF1B1B1F),
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    error = Color(0xFFCF6679),
    onError = Color.Black
)

@Composable
fun HalifaxBusAppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography(),
        content = content
    )
}
