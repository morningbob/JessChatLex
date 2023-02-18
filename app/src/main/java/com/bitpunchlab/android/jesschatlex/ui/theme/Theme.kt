package com.bitpunchlab.android.jesschatlex.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = JessChatLex.titleColor,
    primaryVariant = JessChatLex.contentColor,
    secondary = JessChatLex.buttonColor
)

private val LightColorPalette = lightColors(
    primary = JessChatLex.titleColor,
    primaryVariant = JessChatLex.contentColor,
    secondary = JessChatLex.buttonColor,
    background = JessChatLex.background,
    //buttonColor = JessChatLex.buttonColor

    /* Other default colors to override

    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun JessChatLexTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}