package com.msharialsayari.musrofaty.widget.financial

import android.content.Context
import android.util.Log
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback

class RefreshFinancialClickAction : ActionCallback {

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
       Log.i("RefreshFinancialClickAction","Clicked")
        FinancialWidget().update(context, glanceId)
    }
}