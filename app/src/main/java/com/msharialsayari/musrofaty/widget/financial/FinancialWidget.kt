package com.msharialsayari.musrofaty.widget.financial

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.*
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding

class FinancialWidget : GlanceAppWidget() {
    @Composable
    override fun Content() {

        FinancialContent(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(
                    day = Color.Blue,
                    night = Color.DarkGray
                )
                .appWidgetBackground()
                .cornerRadius(16.dp)
                .padding(8.dp),
        )
    }
}


class FinancialWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = FinancialWidget()
}