package com.msharialsayari.musrofaty.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme

@Stable
fun Modifier.mirror(): Modifier = composed {
    if (MusrofatyTheme.layoutDirection == LayoutDirection.Rtl)
        this.scale(scaleX = -1f, scaleY = 1f)
    else
        this
}

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}

@Composable
fun rememberWindowInsetsController(): WindowInsetsControllerCompat {
    val window = with(LocalContext.current as Activity) { return@with window }
    return remember { WindowCompat.getInsetsController(window, window.decorView) }
}


@Composable
fun SetSystemBarsColorOnDarkScreens() {
    val windowController = rememberWindowInsetsController()
    val isLightTheme = MusrofatyTheme.isDarkMode
    DisposableEffect(key1 = Unit) {
        onDispose {
            windowController.toggleLightMode(forceLight = null, isLight = isLightTheme)
        }
    }

    LaunchedEffect(key1 = Unit) {
        windowController.toggleLightMode(forceLight = false, isLight = isLightTheme)
    }
}

fun WindowInsetsControllerCompat.toggleLightMode(forceLight: Boolean?, isLight: Boolean) {
    if (forceLight != null) {
        isAppearanceLightStatusBars = forceLight
        isAppearanceLightNavigationBars = forceLight
    } else {
        isAppearanceLightStatusBars = isLight
        isAppearanceLightNavigationBars = isLight
    }
}

fun WindowInsetsControllerCompat.toggleSystemBars(show: Boolean) {
    if (show) show(WindowInsetsCompat.Type.systemBars())
    else hide(WindowInsetsCompat.Type.systemBars())
}

fun WindowInsetsControllerCompat.toggleSystemNavigationBars(show: Boolean) {
    if (show) show(WindowInsetsCompat.Type.navigationBars())
    else hide(WindowInsetsCompat.Type.navigationBars())
}

@Composable
fun SetStatusBarColor(color: Color = MusrofatyTheme.colors.statusBarColor) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !MusrofatyTheme.isDarkMode


    SideEffect {
        systemUiController.setStatusBarColor(
            color = color,
            darkIcons = false
        )
    }
}

@Composable
fun SetNavigationBarColor(color: Color = MusrofatyTheme.colors.navigationBarColor) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !MusrofatyTheme.isDarkMode

    SideEffect {
        systemUiController.setNavigationBarColor(
            color = color,
            darkIcons = false
        )
    }
}