package com.msharialsayari.musrofaty.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.msharialsayari.musrofaty.ui.theme.MusrofatyTheme

import java.util.*

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
fun SetStatusBarColor(color: Color? = MusrofatyTheme.colors.toolbarColor) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !MusrofatyTheme.isDarkMode
    val statusBarColor = color ?: MusrofatyTheme.colors.toolbarColor

    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = false
        )
    }
}