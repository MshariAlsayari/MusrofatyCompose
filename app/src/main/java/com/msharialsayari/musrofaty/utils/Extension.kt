package com.msharialsayari.musrofaty.utils

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.msharialsayari.musrofaty.utils.enums.ScreenType

@ColorInt
fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}


fun Int.toDp() = (this /  Resources.getSystem().displayMetrics.density).toInt()

fun Int.toPx() = (this *  Resources.getSystem().displayMetrics.density).toInt()

fun DisplayMetrics.getScreenTypeByWidth(): ScreenType {
    val width = this.widthPixels.toDp()

    return when{
        width in 600..839 -> ScreenType.Medium
        width > 840 -> ScreenType.Expanded
        else -> ScreenType.Compact
    }

}