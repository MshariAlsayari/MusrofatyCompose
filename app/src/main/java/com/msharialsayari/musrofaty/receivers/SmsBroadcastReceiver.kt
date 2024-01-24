package com.msharialsayari.musrofaty.receivers


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.msharialsayari.musrofaty.jobs.InsertLatestSmsJob
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
            val insertLatestSmsJobWorker = OneTimeWorkRequestBuilder<InsertLatestSmsJob>().build()
            WorkManager.getInstance(context).enqueue(insertLatestSmsJobWorker)

            val updateAppWidgetWorker = OneTimeWorkRequestBuilder<UpdateAppWidgetJob>().build()
            WorkManager.getInstance(context).enqueue(updateAppWidgetWorker)

        }

    }
}