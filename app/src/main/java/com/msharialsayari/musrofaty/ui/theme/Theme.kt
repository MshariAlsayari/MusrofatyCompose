package com.msharialsayari.musrofaty.ui.theme

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat
import androidx.core.view.WindowCompat
import com.msharialsayari.musrofaty.business_layer.domain_layer.settings.Language
import com.msharialsayari.musrofaty.business_layer.domain_layer.settings.Theme
import com.msharialsayari.musrofaty.utils.enums.ScreenType
import java.util.Locale


@Composable
fun MusrofatyComposeTheme(
    appTheme: Theme = Theme.DARK,
    appLanguage: Language = Language.DEFAULT,
    screenType: ScreenType,
    activity: ComponentActivity? = null,
    content: @Composable () -> Unit,
) {


    val colors = getColorSchema(appTheme)
    val context = LocalContext.current
    val local = getDeviceLocal()
    val isDark = colors.second
    val view = LocalView.current

    val direction = when (appLanguage) {
        Language.DEFAULT -> if (local.language.lowercase() == "en") LayoutDirection.Ltr else LayoutDirection.Rtl
        Language.ARABIC -> LayoutDirection.Rtl
        Language.ENGLISH -> LayoutDirection.Ltr
    }

    val shortcut = when (direction) {
        LayoutDirection.Rtl -> Language.ARABIC.shortcut
        else -> Language.ENGLISH.shortcut

    }

    context.resources.apply {
        val locale = Locale(shortcut)
        val config = Configuration(configuration)

        context.createConfigurationContext(configuration)
        Locale.setDefault(locale)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, displayMetrics)
    }


    activity?.let {
        EnableEdgeToEdge(it,isDark)
    }

    val statusBarColor = MusrofatyTheme.colors.statusBarColor
    val navigationBarColor = MusrofatyTheme.colors.navigationBarColor
    if(!view.isInEditMode){
        SideEffect {
            val window = ( view.context as Activity).window
            window.statusBarColor = statusBarColor.toArgb()
            window.navigationBarColor = navigationBarColor.toArgb()
            WindowCompat.getInsetsController(window,view).isAppearanceLightStatusBars = !isDark
            WindowCompat.getInsetsController(window,view).isAppearanceLightNavigationBars = !isDark
        }
    }

    ProvideMusrofatyTheme(colors.first,screenType, colors.second, direction) {
        CompositionLocalProvider(LocalLayoutDirection provides direction) {
            MaterialTheme(
                colors = mapBasicColors(colors= colors.first, darkTheme = colors.second),
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
    isDarkMode: Boolean,
    layoutDirection:LayoutDirection,
    content: @Composable () -> Unit
) {
    val rememberedColors = remember {
        colors.copy()
    }.apply { updateColorsFrom(colors) }


    CompositionLocalProvider(
        LocalCustomColors provides rememberedColors,
        LocalCustomScreenType provides  screenType,
        LocalCustomCurrentMode provides isDarkMode,
        LocalCustomCurrentLayoutDirection provides layoutDirection,
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

    val isDarkMode: Boolean
        @Composable
        get() = LocalCustomCurrentMode.current

    val layoutDirection: LayoutDirection
        @Composable
        get() = LocalCustomCurrentLayoutDirection.current



}

private val LocalCustomColors = staticCompositionLocalOf {
    MusrofatyColors()
}

private val LocalCustomScreenType = staticCompositionLocalOf {
    ScreenType.Compact
}

private val LocalCustomCurrentMode = staticCompositionLocalOf {
    true
}

private val LocalCustomCurrentLayoutDirection = staticCompositionLocalOf {
    LayoutDirection.Ltr
}


@Composable
fun getColorSchema(appTheme: Theme): Pair<MusrofatyColors,Boolean> {
    return when (appTheme) {
        Theme.LIGHT -> LightColors to false
        Theme.DARK -> DarkColors to true
        else -> if (isSystemInDarkTheme()) {
            DarkColors to true
        } else {
            LightColors to false
        }
    }
}

@Composable
@ReadOnlyComposable
fun getDeviceLocal(): Locale {
    val configuration = LocalConfiguration.current
    val locale = ConfigurationCompat.getLocales(configuration).get(0) ?: LocaleListCompat.getDefault()[0]!!
    return locale
}
@Composable
private fun EnableEdgeToEdge(activity: ComponentActivity, isDark:Boolean){

    val systemBarStyle = if(isDark){
        SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
    }else{
        SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
    }
    activity.enableEdgeToEdge(
        statusBarStyle = systemBarStyle,
        navigationBarStyle = systemBarStyle,
    )

}
