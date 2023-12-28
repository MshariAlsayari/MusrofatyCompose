package com.msharialsayari.musrofaty.widgets


import androidx.glance.appwidget.GlanceAppWidgetReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.CallSuper

abstract class HiltGlanceAppWidgetReceiver : GlanceAppWidgetReceiver() {
    @CallSuper
    override fun onReceive(context: Context, intent: Intent) {
    }
}