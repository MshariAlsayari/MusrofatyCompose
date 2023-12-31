package com.msharialsayari.musrofaty

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.msharialsayari.musrofaty.jobs.UpdateAppWidgetJob
import dagger.hilt.android.HiltAndroidApp
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltAndroidApp
class MyApp : Application(), Configuration.Provider {

    companion object {
        private val TAG = MyApp::class.java.simpleName
    }

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        val workRequest = PeriodicWorkRequest.Builder(UpdateAppWidgetJob::class.java, 1, TimeUnit.DAYS)
            .setConstraints(Constraints.NONE)
            .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
            .build()
         WorkManager.getInstance(this).enqueueUniquePeriodicWork(
             UpdateAppWidgetJob.TAG,
             ExistingPeriodicWorkPolicy.KEEP,
             workRequest)
    }

    private fun calculateInitialDelay(): Long {
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0) // Set the hour to 12:00 a.m.
        calendar.set(Calendar.MINUTE, 0) // Set the minute to 0
        calendar.set(Calendar.SECOND, 0) // Set the second to 0
        val currentTimeMillis = System.currentTimeMillis()
        val scheduledTimeMillis: Long = calendar.timeInMillis

        // Calculate the initial delay to reach 12:00 a.m.
        var initialDelayMillis = scheduledTimeMillis - currentTimeMillis

        // Ensure the initial delay is positive (avoid negative delays).
        if (initialDelayMillis < 0) {
            initialDelayMillis += TimeUnit.DAYS.toMillis(1) // Add 1 day if it's negative
        }
        Log.d(TAG, "calculateInitialDelay() initialDelayMillis: $initialDelayMillis")
        return initialDelayMillis
    }
}