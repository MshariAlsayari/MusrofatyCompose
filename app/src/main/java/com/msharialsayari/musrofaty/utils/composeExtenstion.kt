package com.msharialsayari.musrofaty.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.LayoutDirection
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.core.text.layoutDirection

import java.util.*

@Stable
fun Modifier.mirror(): Modifier {
    return if (Locale.getDefault().layoutDirection == LayoutDirection.RTL)
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