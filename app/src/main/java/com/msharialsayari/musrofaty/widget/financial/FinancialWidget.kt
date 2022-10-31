package com.msharialsayari.musrofaty.widget.financial

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.*
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.msharialsayari.musrofaty.ui.theme.BlackOnyx
import com.msharialsayari.musrofaty.ui.theme.White

class FinancialWidget : GlanceAppWidget() {
    override var stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition



    @Composable
    override fun Content() {
       // val viewModel:DashboardViewModel = hiltViewModel()


        FinancialContent(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(day = White, night = BlackOnyx)
                .appWidgetBackground()
                .cornerRadius(16.dp)
                .padding(8.dp),
        )
    }

    companion object {
        const val FINANCIAL_WIDGET_INCOME_PREFS_KEY           = "FINANCIAL_WIDGET_INCOME_PREFS_KEY"
        const val FINANCIAL_WIDGET_EXPENSES_PREFS_KEY         = "FINANCIAL_WIDGET_EXPENSES_PREFS_KEY"
        const val FINANCIAL_WIDGET_PERCENT_INCOME_PREFS_KEY   = "FINANCIAL_WIDGET_PERCENT_INCOME_PREFS_KEY"
        const val FINANCIAL_WIDGET_PERCENT_EXPENSES_PREFS_KEY = "FINANCIAL_WIDGET_PERCENT_EXPENSES_PREFS_KEY"
    }
}


class FinancialWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = FinancialWidget()
}