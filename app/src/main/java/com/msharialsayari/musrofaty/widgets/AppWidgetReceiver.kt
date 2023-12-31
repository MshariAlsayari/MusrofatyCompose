package com.msharialsayari.musrofaty.widgets

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.msharialsayari.musrofaty.jobs.UpdateAppWidgetJob
import dagger.hilt.android.AndroidEntryPoint

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
        WorkManager.getInstance(context).enqueueUniqueWork(
            UpdateAppWidgetJob.TAG,
            ExistingWorkPolicy.REPLACE,
            updateAppWidgetWorker
        )
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.d(TAG, "onUpdate()")
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        Log.d(TAG, "onDeleted()")
    }

}