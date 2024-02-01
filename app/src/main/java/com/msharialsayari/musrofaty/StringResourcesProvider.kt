package com.msharialsayari.musrofaty

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class StringResourcesProvider @Inject constructor(@ApplicationContext private val context: Context) {

    fun getString(@StringRes stringId: Int, vararg format: Any): String {
        return context.getString(stringId, *format)
    }

    fun getString(@StringRes stringId: Int): String {
        return context.getString(stringId)
    }

    fun getQuantityString(stringId: Int, quantity: Int): String {
        return context.resources.getQuantityString(stringId, quantity, quantity)
    }

    fun getQuantityString(stringId: Int, quantity: Int, vararg formatArgs: Any): String {
        return context.resources.getQuantityString(stringId, quantity, *formatArgs)
    }
}