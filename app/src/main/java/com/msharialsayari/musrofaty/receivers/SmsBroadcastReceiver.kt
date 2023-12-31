package com.msharialsayari.musrofaty.receivers


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.msharialsayari.musrofaty.jobs.UpdateAppWidgetJob
import com.msharialsayari.musrofaty.widgets.AppWidget
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SmsBroadcastReceiver : BroadcastReceiver(){


    private val scope = CoroutineScope(Dispatchers.Main)


    companion object {
        private val TAG = SmsBroadcastReceiver::class.java.simpleName
        private const val SMS = "android.provider.Telephony.SMS_RECEIVED"
    }



    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "onReceive() ")
        if(intent.action.equals(SMS)){
            scope.launch {
                val glanceId = GlanceAppWidgetManager(context).getGlanceIds(AppWidget::class.java).firstOrNull()
                glanceId?.let {
                    updateAppWidgetState(context, it) { mutablePreferences ->
                        mutablePreferences[AppWidget.LOADING_WIDGET_PREF_KEY] = true
                        AppWidget().update(context, it)
                    }
                }

            }

            val updateAppWidgetWorker = OneTimeWorkRequestBuilder<UpdateAppWidgetJob>().build()



            WorkManager.getInstance(context).enqueueUniqueWork(
                UpdateAppWidgetJob.TAG,
                ExistingWorkPolicy.REPLACE,
                updateAppWidgetWorker
            )

            scope.launch {
                val glanceId = GlanceAppWidgetManager(context).getGlanceIds(AppWidget::class.java).firstOrNull()
                glanceId?.let {
                    updateAppWidgetState(context, it) { mutablePreferences ->
                        mutablePreferences[AppWidget.LOADING_WIDGET_PREF_KEY] = false
                        AppWidget().update(context, it)
                    }
                }
            }
        }

    }
}