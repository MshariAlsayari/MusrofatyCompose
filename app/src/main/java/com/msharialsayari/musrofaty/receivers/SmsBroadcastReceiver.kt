package com.msharialsayari.musrofaty.receivers


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.msharialsayari.musrofaty.jobs.UpdateAppWidgetJob
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SmsBroadcastReceiver : BroadcastReceiver(){




    companion object {
        private val TAG = SmsBroadcastReceiver::class.java.simpleName
        private const val SMS = "android.provider.Telephony.SMS_RECEIVED"
    }



    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "onReceive() ")
        if(intent.action.equals(SMS)){
            val updateAppWidgetWorker = OneTimeWorkRequestBuilder<UpdateAppWidgetJob>().build()
            WorkManager.getInstance(context).enqueue(updateAppWidgetWorker)
        }

    }
}