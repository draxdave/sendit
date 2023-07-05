package com.drax.sendit.view.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@Composable
fun ComposeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = when {
        darkTheme -> DarkColorPalette
        else -> LightColorPalette
    }

    ProvideComposeColors(colors) {
        MaterialTheme(
            colors = colors.materialColors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}

private val DarkColorPalette = ComposeColors(
    material = darkColors(
        primary = receiverTextBackground,
        primaryVariant = warning_warmer,
        secondary = aqua200,
        secondaryVariant = antique_brass,
        surface = raisin_black,
    ),
    statusBar = main_text_lighter,
    contextualStatusBar = black,
    appBar = main_text_lighter,
    contextualAppBar = black,
    appBarContent = Color.White,
    contextualAppBarContent = Color.White,
    secondaryBackground = colorPrimaryDark,
    isLight = false
)

private val LightColorPalette = ComposeColors(
    material = lightColors(
        primary = colorPrimary,
        primaryVariant = colorAccent,
        secondaryVariant = wine,
        secondary = colorPrimaryText,
        surface = raisin_black,
    ),
    statusBar = senderTextBackground,
    contextualStatusBar = senderTextBackground,
    appBar = receiverTextBackground,
    contextualAppBar = receiverTextBackground,
    appBarContent = Color.White,
    contextualAppBarContent = Color.White,
    secondaryBackground = lightAqua,
    isLight = true
)

@Composable
fun ProvideComposeColors(
    colors: ComposeColors,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalComposeColors provides colors, content = content)
}

private val LocalComposeColors = staticCompositionLocalOf<ComposeColors> {
    error("No LocalComposeColors provided")
}

object ComposeTheme {
    val colors: ComposeColors
        @Composable
        get() = LocalComposeColors.current
}

@Stable
class ComposeColors(
    material: Colors,
    statusBar: Color,
    contextualStatusBar: Color,
    appBar: Color,
    contextualAppBar: Color,
    appBarContent: Color,
    contextualAppBarContent: Color,
    secondaryBackground: Color,
    isLight: Boolean
) {
    var materialColors by mutableStateOf(material)
        private set

    var isLight by mutableStateOf(isLight)
        private set

    var statusBar by mutableStateOf(statusBar)
        private set

    var contextualStatusBar by mutableStateOf(contextualStatusBar)
        private set

    var appBar by mutableStateOf(appBar)
        private set

    var contextualAppBar by mutableStateOf(contextualAppBar)
        private set

    var appBarContent by mutableStateOf(appBarContent)
        private set

    var contextualAppBarContent by mutableStateOf(contextualAppBarContent)
        private set

    var secondaryBackgroundColor by mutableStateOf(secondaryBackground)
        private set
}