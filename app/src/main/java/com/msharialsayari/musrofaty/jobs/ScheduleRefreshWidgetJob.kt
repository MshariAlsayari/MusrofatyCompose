package com.msharialsayari.musrofaty.jobs

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.msharialsayari.musrofaty.utils.DateUtils
import java.util.concurrent.TimeUnit

object ScheduleRefreshWidgetJob {

    private val TAG = ScheduleRefreshWidgetJob::class.java.simpleName
     fun initUpdateAppWidgetJobWorker(context: Context) {
        val timeDelay = DateUtils.calculateInitialDelayForSchedulingJobs()
        val formattedTime = DateUtils.formatInitialDelayToHours(timeDelay)
        Log.i(TAG, "initUpdateAppWidgetJobWorker() time delay to run the job is $formattedTime")

        val workRequest =
            PeriodicWorkRequest.Builder(
                UpdateAppWidgetJob::class.java,
                1,
                TimeUnit.DAYS
            )
                .setInitialDelay(timeDelay, TimeUnit.MILLISECONDS)
                .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            UpdateAppWidgetJob.TAG,
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }
}