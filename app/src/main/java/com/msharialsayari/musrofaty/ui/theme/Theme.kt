package com.msharialsayari.musrofaty.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.msharialsayari.musrofaty.utils.AppTheme

private val DarkColorPalette = darkColors(
    primary = PrimaryColor,
    primaryVariant = PrimaryDarkColor,
    secondary = SecondaryColor,
    secondaryVariant = SecondaryColor,
    background = BlackOnyx,
    surface = BlackOnyx,
    onPrimary = White,
    onSecondary = White,
)

private val LightColorPalette = lightColors(
    primary = PrimaryDarkColor,
    primaryVariant = PrimaryDarkColor,
    secondary = PrimaryDarkColor,
    secondaryVariant = SecondaryColor,
    background = White,
    surface = White,
    onPrimary = White,
    onSecondary = White
)

@Composable
fun MusrofatyComposeTheme(
    appTheme: AppTheme = AppTheme.System,
    content: @Composable () -> Unit
) {
    val colors = if (isLightTheme(appTheme)){
        LightColorPalette
    }else
        DarkColorPalette




        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
}


@Composable
fun isLightTheme(appTheme: AppTheme):Boolean{
    return when(appTheme){
        AppTheme.Light -> true
        AppTheme.Dark -> false
        AppTheme.System -> !isSystemInDarkTheme()
    }
}
