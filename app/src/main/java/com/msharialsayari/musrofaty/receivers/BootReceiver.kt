package com.msharialsayari.musrofaty.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.msharialsayari.musrofaty.jobs.ScheduleRefreshWidgetJob
import com.msharialsayari.musrofaty.jobs.UpdateAppWidgetJob
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    companion object {
        private val TAG = BootReceiver::class.java.simpleName
        private const val BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "onReceive()")
        if (intent.action.equals(BOOT_COMPLETED)) {
            val updateAppWidgetWorker = OneTimeWorkRequestBuilder<UpdateAppWidgetJob>().build()
            WorkManager.getInstance(context).enqueue(updateAppWidgetWorker)

            ScheduleRefreshWidgetJob.initUpdateAppWidgetJobWorker(context)
        }
    }


}