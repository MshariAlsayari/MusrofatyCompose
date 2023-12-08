package com.msharialsayari.musrofaty.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.msharialsayari.musrofaty.utils.AppTheme
import com.msharialsayari.musrofaty.utils.Constants
import com.msharialsayari.musrofaty.utils.enums.ScreenType
import java.util.*


@Composable
fun MusrofatyComposeTheme(
    appTheme: AppTheme = AppTheme.System,
    appLocale: Locale,
    screenType: ScreenType,
    content: @Composable () -> Unit,
) {
    val colors = if (isLightTheme(appTheme)){
        LightColors
    }else
        DarkColors

    val direction = if (appLocale.language.lowercase() == Constants.arabic_ar.lowercase()) LayoutDirection.Rtl else LayoutDirection.Ltr

    ProvideMusrofatyTheme(colors,screenType) {
        CompositionLocalProvider(LocalLayoutDirection provides direction) {
            MaterialTheme(
                colors = mapBasicColors(colors= colors, darkTheme = !colors.isLight),
                typography = Typography,
                shapes = Shapes,
                content = content
            )
        }
    }
}


@Composable
fun ProvideMusrofatyTheme(
    colors: MusrofatyColors,
    screenType: ScreenType,
    content: @Composable () -> Unit
) {
    val rememberedColors = remember {
        colors.copy()
    }.apply { updateColorsFrom(colors) }


    CompositionLocalProvider(
        LocalCustomColors provides rememberedColors,
        LocalCustomScreenType provides  screenType,
        content = content
    )
}

object MusrofatyTheme {
    val colors: MusrofatyColors
        @Composable
        get() = LocalCustomColors.current

    val screenType: ScreenType
        @Composable
        get() = LocalCustomScreenType.current


}

private val LocalCustomColors = staticCompositionLocalOf {
    MusrofatyColors()
}

private val LocalCustomScreenType = staticCompositionLocalOf {
    ScreenType.Compact
}


@Composable
fun isLightTheme(appTheme: AppTheme):Boolean{
    return when(appTheme){
        AppTheme.Light -> true
        AppTheme.Dark -> false
        AppTheme.System -> !isSystemInDarkTheme()
    }
}
