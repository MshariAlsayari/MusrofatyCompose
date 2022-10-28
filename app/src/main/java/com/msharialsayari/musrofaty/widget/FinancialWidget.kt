package com.msharialsayari.musrofaty.widget

import androidx.compose.runtime.Composable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class FinancialWidget : GlanceAppWidget() {
    @Composable
    override fun Content() {
    }
}


class FinancialWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = FinancialWidget()
}