package com.example.milkchequedemo.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = MexicanRed,
    onPrimary = White,
    primaryContainer = BabyRed,
    onPrimaryContainer = CodGray,

    secondary = Hurricane,
    onSecondary = White,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = CodGray,

    background = HintOfRed,
    onBackground = CodGray,
    surface = HintOfRed,
    onSurface = CodGray,
    outline = Hurricane
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF8B8B),          // lighter red so it pops on dark
    onPrimary = CodGray,                // better contrast on the light red
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = HintOfRed,

    secondary = Color(0xFFA89595),        // lighter Hurricane for dark mode
    onSecondary = CodGray,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = HintOfRed,

    background = CodGray,
    onBackground = HintOfRed,
    surface = CodGray,
    onSurface = HintOfRed,
    outline = Hurricane
)

@Composable
fun MilkChequeDemoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Set to false if you *always* want your custom palette on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val ctx = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(ctx) else dynamicLightColorScheme(ctx)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
