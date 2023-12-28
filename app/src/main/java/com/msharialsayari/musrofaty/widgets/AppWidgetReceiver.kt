package com.msharialsayari.musrofaty.widgets

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetFinancialStatisticsUseCase
import com.msharialsayari.musrofaty.business_layer.domain_layer.usecase.GetSmsListUseCase
import com.msharialsayari.musrofaty.jobs.UpdateAppWidgetJob
import com.msharialsayari.musrofaty.receivers.SmsBroadcastReceiver
import com.msharialsayari.musrofaty.receivers.goAsync
import com.msharialsayari.musrofaty.utils.Constants
import com.msharialsayari.musrofaty.utils.DateUtils
import com.msharialsayari.musrofaty.utils.StringsUtils
import com.msharialsayari.musrofaty.utils.models.FinancialStatistics
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AppWidgetReceiver : GlanceAppWidgetReceiver() {



    companion object {
        val TAG = AppWidgetReceiver::class.java.simpleName
    }

    override val glanceAppWidget: GlanceAppWidget
        get() = AppWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        Log.d(TAG, "onReceive()")
        val updateAppWidgetWorker = OneTimeWorkRequestBuilder<UpdateAppWidgetJob>().build()
        WorkManager.getInstance(context).enqueue(updateAppWidgetWorker)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.d(TAG, "onUpdate()")
        val updateAppWidgetWorker = OneTimeWorkRequestBuilder<UpdateAppWidgetJob>().build()
        WorkManager.getInstance(context).enqueue(updateAppWidgetWorker)
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        Log.d(TAG, "onDeleted()")
    }

}